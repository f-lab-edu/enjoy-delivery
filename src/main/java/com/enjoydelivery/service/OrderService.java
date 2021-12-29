package com.enjoydelivery.service;

import com.enjoydelivery.dao.OrderDAO;
import com.enjoydelivery.dto.cart.response.OrderItemResponseDTO;
import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.dto.order.request.CreateOrderRequestDTO;
import com.enjoydelivery.dto.order.response.SendOrderMessageDTO;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.Owner;
import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.entity.Store;
import com.enjoydelivery.entity.User;
import com.enjoydelivery.exception.QueueFullException;
import com.enjoydelivery.repository.OrderRepository;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private static final int RETRY_COUNT_LIMIT = 3;
  private static final String REQUEST_ORDER_TITLE = "주문 요청이 들어왔습니다.";
  private final OrderRepository orderRepository;
  private final CartService cartService;
  private final PayService payService;
  private final OrderItemService orderItemService;
  private final UserService userService;
  private final MenuService menuService;
  private final OrderDAO orderDAO;
  private final PushService pushService;

  @Transactional
  public void order(long userId, CreateOrderRequestDTO requestDTO) {
    User user = userService.readOneById(userId);

   ReadCartResponseDTO cart = cartService.read(userId);

   Pay pay = requestDTO.createPayEntity();
   payService.save(pay);

   Order order = requestDTO.createOrderEntity();
   order.setUser(user);
   order.setPay(pay);
   orderRepository.save(order);

    List<Long> menuIds = getMenuIds(cart.getOrderItemResponseDTOS());
    List<Menu> menus = menuService.readAllByIds(menuIds);
    List<Integer> counts = getCounts(cart.getOrderItemResponseDTOS());
    List<OrderItem> orderItems = createOrderItems(menus, order, counts);
    order.setOrderItems(orderItems);

    orderItemService.saveAll(orderItems);

    Store store = menus.get(0).getStore();
    Owner owner = store.getOwner();
    String ownerPushToken = orderDAO.getOwnerToken(owner.getId());
    sendOrderMessage(ownerPushToken, order);
  }

  private void sendOrderMessage(String ownerPushToken, Order order)  {

    Notification notification = Notification.builder()
        .setTitle(REQUEST_ORDER_TITLE)
        .setBody(createNotificationBody(order))
        .build();

    Message message = Message.builder()
        .setToken(ownerPushToken)
        .setNotification(notification)
        .build();

    try {
      pushService.sendMessage(message);
    } catch (RejectedExecutionException re) {//대기큐가 가득차면 발생하는 에러
      waitAndResendOrderMessage(message, 1);
    }
  }


  private void waitAndResendOrderMessage(Message message, int count) {
    if (count > RETRY_COUNT_LIMIT) {
      throw new QueueFullException();
    }

    try {
      Thread.sleep(1000);//1초간 실행 -> waiting 일시정지 상태가 됨.
    } catch (InterruptedException ie) {
      //지정한 시간이 다되거나 interrupt가 호출되면 interruptedException 발생
      Thread.currentThread().interrupt();//waiting -> runnable
    }

    try {
      pushService.sendMessage(message);
    } catch (RejectedExecutionException re) {
      waitAndResendOrderMessage(message, count++);
    }

  }

  private String createNotificationBody(Order order) {
    return SendOrderMessageDTO
        .create(order)
        .createNotificationBody();
  }


  private List<OrderItem> createOrderItems(List<Menu> menus, Order order, List<Integer> counts) {

    return IntStream.range(0, menus.size())
        .mapToObj(i -> createOrderItem(menus.get(i), order, counts.get(i)))
        .collect(Collectors.toList());
  }

  private OrderItem createOrderItem(Menu menu, Order order, int count){
    return OrderItem.builder()
        .order(order)
        .menu(menu)
        .count(count)
        .build();
  }

  private List<Long> getMenuIds(List<OrderItemResponseDTO> orderItemResponseDTOS) {
    return orderItemResponseDTOS.stream()
        .map(oi -> oi.getMenuCommand().getId())
        .collect(Collectors.toList());
  }

  private List<Integer> getCounts(List<OrderItemResponseDTO> orderItemResponseDTOS) {
    return orderItemResponseDTOS.stream()
        .map(OrderItemResponseDTO::getCount)
        .collect(Collectors.toList());
  }

}

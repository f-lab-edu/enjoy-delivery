package com.enjoydelivery.service;

import com.enjoydelivery.dto.cart.response.OrderItemResponseDTO;
import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.dto.order.request.CreateOrderRequestDTO;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.entity.User;
import com.enjoydelivery.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final CartService cartService;
  private final PayService payService;
  private final OrderItemService orderItemService;
  private final UserService userService;
  private final MenuService menuService;

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

    orderItemService.saveAll(orderItems);
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

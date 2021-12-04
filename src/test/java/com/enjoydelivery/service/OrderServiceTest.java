package com.enjoydelivery.service;

import static com.enjoydelivery.service.StoreServiceTest.makeStore;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.dto.order.request.CreateOrderRequestDTO;
import com.enjoydelivery.dto.order.request.PayRequestDTO;
import com.enjoydelivery.entity.Menu;
import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.OrderState;
import com.enjoydelivery.entity.Pay;
import com.enjoydelivery.entity.PayState;
import com.enjoydelivery.entity.PayType;
import com.enjoydelivery.entity.Store;
import com.enjoydelivery.entity.User;
import com.enjoydelivery.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @InjectMocks
  OrderService sut;

  @Mock
  OrderRepository orderRepository;

  @Mock
  CartService cartService;

  @Mock
  MenuService menuService;

  @Mock
  StoreService storeService;

  @Mock
  PayService payService;

  @Mock
  UserService userService;

  @Mock
  OrderItemService orderItemService;

  public static PayRequestDTO makePayRequestDTO(PayType payType) {
    return PayRequestDTO.builder()
        .payType(payType)
        .originCost(40000)
        .discountCost(0)
        .build();
  }
  public static CreateOrderRequestDTO makeCreateOrderRequestDTO(PayRequestDTO payRequestDTO) {
    return CreateOrderRequestDTO.builder()
        .address("인천광역시 부평구 산곡동 100번지")
        .phoneNumber("01012345678")
        .description("리뷰 이벤트 참여")
        .payRequestDTO(payRequestDTO)
        .build();
  }
  public static ReadCartResponseDTO makeCart(Store store, List<OrderItem> orderItems) {
    return new ReadCartResponseDTO(
        store.getId(),
        store.getName(),
        store.getDeliveryCost(),
        orderItems);
  }

  @Test
  public void sut_make_pay_state_change_into_결제_전_if_pay_type_is_만나서_결제 () {

    // Arrange
    PayRequestDTO payRequestDTO
        = makePayRequestDTO(PayType.만나서결제);

    CreateOrderRequestDTO orderRequestDTO
        = makeCreateOrderRequestDTO(payRequestDTO);

    Store store = makeStore();
    Pay pay = payRequestDTO.createEntity();
    Menu menu1 = Menu.builder()
        .id(8L)
        .name("메뉴1")
        .price(10000)
        .build();

    Menu menu2 = Menu.builder()
        .id(9L)
        .name("메뉴2")
        .price(20000)
        .build();

    OrderItem item1 = OrderItem.builder()
        .menu(menu1)
        .count(2)
        .build();

    OrderItem item2 = OrderItem.builder()
        .menu(menu2)
        .count(1)
        .build();

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(item1);
    orderItems.add(item2);
    List<Menu> menus = new ArrayList<>();
    menus.add(menu1);
    menus.add(menu2);

    List<Long> menuIds = new ArrayList<>();
    menuIds.add(menu1.getId());
    menuIds.add(menu2.getId());

    ReadCartResponseDTO cart = makeCart(store, orderItems);

    User user = User.builder()
        .id(1L)
        .build();

    Order order = orderRequestDTO.createOrderEntity();
    order.setUser(user);
    order.setPay(pay);

    long userId = user.getId();

    doReturn(user)
        .when(userService)
        .readOneById(userId);

    doReturn(cart)
        .when(cartService)
        .read(userId);

    doReturn(menus)
        .when(menuService)
        .readAllByIds(menuIds);


    // Act
    sut.order(userId, orderRequestDTO);

    // Assert
    assertThat(order.getOrderState())
        .isEqualTo(OrderState.주문접수);


    assertThat(pay.getPayState())
        .isEqualTo(PayState.결제전);

  }


  @Test
  public void sut_make_pay_state_change_into_결제_완료_if_pay_type_is_바로_결제() {

    // Arrange
    PayRequestDTO payRequestDTO
        = makePayRequestDTO(PayType.바로결제);

    CreateOrderRequestDTO orderRequestDTO
        = makeCreateOrderRequestDTO(payRequestDTO);

    Store store = makeStore();
    Pay pay = payRequestDTO.createEntity();
    Menu menu1 = Menu.builder()
        .id(8L)
        .name("메뉴1")
        .price(10000)
        .build();

    Menu menu2 = Menu.builder()
        .id(9L)
        .name("메뉴2")
        .price(20000)
        .build();

    OrderItem item1 = OrderItem.builder()
        .menu(menu1)
        .count(2)
        .build();

    OrderItem item2 = OrderItem.builder()
        .menu(menu2)
        .count(1)
        .build();

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(item1);
    orderItems.add(item2);
    List<Menu> menus = new ArrayList<>();
    menus.add(menu1);
    menus.add(menu2);

    List<Long> menuIds = new ArrayList<>();
    menuIds.add(menu1.getId());
    menuIds.add(menu2.getId());

    ReadCartResponseDTO cart = makeCart(store, orderItems);

    User user = User.builder()
        .id(1L)
        .build();

    Order order = orderRequestDTO.createOrderEntity();
    order.setUser(user);
    order.setPay(pay);

    long userId = user.getId();

    doReturn(user)
        .when(userService)
        .readOneById(userId);

    doReturn(cart)
        .when(cartService)
        .read(userId);

    doReturn(menus)
        .when(menuService)
        .readAllByIds(menuIds);

    // Act
    sut.order(userId, orderRequestDTO);

    // Assert
    assertThat(order.getOrderState())
        .isEqualTo(OrderState.주문접수);


    assertThat(pay.getPayState())
        .isEqualTo(PayState.결제전);
  }

}
package com.enjoydelivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.enjoydelivery.dao.CartDAO;
import com.enjoydelivery.dto.cart.request.CreateOrderItemRequestDTO;
import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.dto.store.request.CategoryInfo;
import com.enjoydelivery.dto.store.request.StoreRequestDTO;
import com.enjoydelivery.dto.store.response.ReadMenuResponseDTO;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.Store;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

  @Mock
  CartDAO cartDAO;

  @Mock
  StoreService storeService;

  @InjectMocks
  CartService sut;

  public static CreateOrderItemRequestDTO makeCreateOrderItemRequestDTO() {
    return new CreateOrderItemRequestDTO(
        1L,
        makeReadMenuResponseDTO(),
        3);
  }

  public static ReadMenuResponseDTO makeReadMenuResponseDTO() {
    return new ReadMenuResponseDTO(
        1L,
        "엽기떡볶이",
        17000);
  }

  public static OrderItem makeOrderItem() {
    return makeOrderItem(makeCreateOrderItemRequestDTO());
  }

  public static OrderItem makeOrderItem(CreateOrderItemRequestDTO createOrderItemRequestDTO) {
    return createOrderItemRequestDTO.toEntity();
  }
  public static StoreRequestDTO makeStoreRequestDTO() {
    return new StoreRequestDTO(
        "00010030",
        "가게이름" ,
        "0320001111",
        "address",
        "thumbnailUrl",
        new CategoryInfo(1L, "한식"),
        "description",
        "12:00",
        "23:00",
        15000,
        3000
    );
  }

  public static Store makeStore() {
    return makeStore(makeStoreRequestDTO());
  }

  public static Store makeStore(StoreRequestDTO storeRequestDTO) {
    Store store = storeRequestDTO.toEntity();
    store.setId(1L);
    return store;
  }

  @Test
  @DisplayName("장바구니가 비어있을 때에는 메뉴를 추가할 수 있다.")
  void sut_add_order_item_when_empty_cart() {

    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = 1L;
    Long storeId = createOrderItemRequestDTO.getStoreId();

    doReturn(0L)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    // Act
    sut.addOrderItem(createOrderItemRequestDTO, userId);

    // Assert
    verify(cartDAO, times(1))
        .addOrderItem(
            argThat(od -> od.getCount() == orderItem.getCount()
                && od.getMenu().getName().equals(orderItem.getMenu().getName())),
            argThat(u -> u.equals(userId)),
            argThat(s -> s.equals(storeId)));


  }

  @Test
  @DisplayName("동일한 가게의 장바구니에 메뉴가 있을 경우에 메뉴를 더 추가할 수 있다")
  void sut_add_order_item_if_there_is_a_item_in_the_cart_of_the_same_store() {
    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = 1L;
    Long storeId = createOrderItemRequestDTO.getStoreId();
    Long menuId = orderItem.getMenu().getId();

    doReturn(storeId)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    doReturn(false)
        .when(cartDAO)
        .existOrderItem(menuId, userId);

    // Act, Assert
    sut.addOrderItem(createOrderItemRequestDTO, userId);

    assertThat(sut.isEmptyCart(storeId))
        .isFalse();

    verify(cartDAO, times(1))
        .addOrderItem(argThat(od -> od.getCount() == orderItem.getCount()
                && od.getMenu().getName().equals(orderItem.getMenu().getName())),
            argThat(u -> u.equals(userId)),
            argThat(s -> s.equals(storeId)));





  }
  @Test
  @DisplayName("다른 가게의 장바구니가 이미 존재할 경우 메뉴룰 넣을 수 없다")
  void sut_failed_to_add_order_item_if_another_store_cart_already_exists() {

    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = 1L;
    Long storeId = createOrderItemRequestDTO.getStoreId();
    Long otherStoreId = 10L;

    doReturn(otherStoreId)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    // Act, Assert
    assertThatThrownBy(() -> {
      sut.addOrderItem(createOrderItemRequestDTO, userId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_INVALID_STORE_ID);

    assertThat(sut.isEmptyCart(otherStoreId))
        .isFalse();

    assertThat(sut.isSameStore(storeId, otherStoreId))
        .isFalse();

    verify(cartDAO, times(0))
        .addOrderItem(orderItem, userId, storeId);

  }

  @Test
  @DisplayName("장바구니에는 이미 담긴 메뉴를 다시 넣을 수 없다.")
  void sut_failed_to_add_order_item_if_the_same_order_item_already_exists() {
    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = 1L;
    Long storeId = createOrderItemRequestDTO.getStoreId();
    Long menuId = orderItem.getMenu().getId();

    doReturn(storeId)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    doReturn(true)
        .when(cartDAO)
        .existOrderItem(menuId, userId);

    // Act, Assert
    assertThatThrownBy(() -> {
      sut.addOrderItem(createOrderItemRequestDTO, userId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_DUPLICATE_MENU);

    assertThat(sut.isEmptyCart(storeId))
        .isFalse();

    verify(cartDAO, times(0))
        .addOrderItem(orderItem, userId, storeId);
  }


  @Test
  @DisplayName("장바구니 메뉴 조회 성공")
  void sut_read_cart_by_user_id() {
    // Arrange
    Long userId = 1L;
    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(makeOrderItem());
    Store store = makeStore();
    Long storeId = store.getId();

    doReturn(orderItems)
        .when(cartDAO).findAllByUserId(userId);

    doReturn(storeId)
        .when(cartDAO).findStoreIdByUserId(userId);

    doReturn(store)
        .when(storeService).readOneById(storeId);

    // Act
    ReadCartResponseDTO readCartResponseDTO
        = sut.read(userId);

    // Assert
    assertThat(readCartResponseDTO.getStoreId())
        .isEqualTo(storeId);

    assertThat(readCartResponseDTO.getStoreName())
        .isEqualTo(store.getName());

    assertThat(readCartResponseDTO.getDeliveryCost())
        .isEqualTo(store.getDeliveryCost());

    assertThat(readCartResponseDTO
        .getOrderItemResponseDTOS().size())
        .isEqualTo(orderItems.size());

    assertThat(readCartResponseDTO
    .getOrderItemResponseDTOS().get(0).getMenuCommand().getId())
        .isEqualTo(orderItems.get(0).getMenu().getId());

    assertThat(readCartResponseDTO
        .getOrderItemResponseDTOS().get(0).getMenuCommand().getName())
        .isEqualTo(orderItems.get(0).getMenu().getName());

    assertThat(readCartResponseDTO
        .getOrderItemResponseDTOS().get(0).getMenuCommand().getPrice())
        .isEqualTo(orderItems.get(0).getMenu().getPrice());
  }



  @Test
  @DisplayName("장바구니 메뉴 조회 실패 : 장바구니가 비어있음.")
  void sut_failed_to_read_cart_when_empty_cart() {
    // Arrange
    Long userId = 1L;
    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(makeOrderItem());
    Long storeId = 0L;

    doReturn(orderItems)
        .when(cartDAO).findAllByUserId(userId);

    doReturn(storeId)
        .when(cartDAO).findStoreIdByUserId(userId);

    // Act
    assertThatThrownBy(() -> {
      ReadCartResponseDTO readCartResponseDTO
          = sut.read(userId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_EMPTY_CART);

    verify(storeService, times(0))
        .readOneById(storeId);
  }

}

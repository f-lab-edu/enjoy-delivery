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
  CartService cartService;

  public CreateOrderItemRequestDTO makeCreateOrderItemRequestDTO() {
    return new CreateOrderItemRequestDTO(
        1L,
        1L,
        makeReadMenuResponseDTO(),
        3);
  }

  public ReadMenuResponseDTO makeReadMenuResponseDTO() {
    return new ReadMenuResponseDTO(
        1L,
        "엽기떡볶이",
        17000);
  }

  public OrderItem makeOrderItem() {
    return makeOrderItem(makeCreateOrderItemRequestDTO());
  }

  public OrderItem makeOrderItem(CreateOrderItemRequestDTO createOrderItemRequestDTO) {
    return createOrderItemRequestDTO.toEntity();
  }
  public StoreRequestDTO makeStoreRequestDTO() {
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

  public Store makeStore() {
    return makeStore(makeStoreRequestDTO());
  }

  public Store makeStore(StoreRequestDTO storeRequestDTO) {
    Store store = storeRequestDTO.toEntity();
    store.setId(1L);
    return store;
  }

  @Test
  @DisplayName("장바구니가 비어있을 때에는 메뉴를 추가할 수 있다.")
  void addOrderItemSuccess() {

    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = createOrderItemRequestDTO.getUserId();
    Long storeId = createOrderItemRequestDTO.getStoreId();

    doReturn(0L)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    // Act
    cartService.addOrderItem(createOrderItemRequestDTO);

    // Assert
    verify(cartDAO, times(1))
        .addOrderItem(
            argThat(od -> od.getCount() == orderItem.getCount()
                && od.getMenu().getName().equals(orderItem.getMenu().getName())),
            argThat(u -> u.equals(userId)),
            argThat(s -> s.equals(storeId)));


  }

  @Test
  @DisplayName("장바구니에 메뉴 넣기 성공 : 이미 장바구니가 채워져있는 경우")
  void addOrderItemSuccess2() {
    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = createOrderItemRequestDTO.getUserId();
    Long storeId = createOrderItemRequestDTO.getStoreId();
    Long menuId = orderItem.getMenu().getId();

    doReturn(storeId)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    doReturn(false)
        .when(cartDAO)
        .existOrderItem(menuId, userId);

    // Act, Assert
    cartService.addOrderItem(createOrderItemRequestDTO);

    assertThat(cartService.isEmptyCart(storeId))
        .isFalse();

    verify(cartDAO, times(1))
        .addOrderItem(argThat(od -> od.getCount() == orderItem.getCount()
                && od.getMenu().getName().equals(orderItem.getMenu().getName())),
            argThat(u -> u.equals(userId)),
            argThat(s -> s.equals(storeId)));





  }
  @Test
  @DisplayName("장바구니에 메뉴 넣기 실패 : 장바구니가 채워져있을 경우 같은 가게의 메뉴만 넣을 수 있다.")
  void addOrderItemFail1() {

    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = createOrderItemRequestDTO.getUserId();
    Long storeId = createOrderItemRequestDTO.getStoreId();
    Long otherStoreId = 10L;

    doReturn(otherStoreId)
        .when(cartDAO)
        .findStoreIdByUserId(userId);

    // Act, Assert
    assertThatThrownBy(() -> {
      cartService.addOrderItem(createOrderItemRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_INVALID_STORE_ID);

    assertThat(cartService.isEmptyCart(otherStoreId))
        .isFalse();

    assertThat(cartService.isSameStore(storeId, otherStoreId))
        .isFalse();

    verify(cartDAO, times(0))
        .addOrderItem(orderItem, userId, storeId);

  }

  @Test
  @DisplayName("장바구니에 메뉴 넣기 실패 : 장바구니에는 이미 담긴 메뉴를 다시 담을 수 없다. ")
  void addOrderItemFail2() {
    // Arrange
    CreateOrderItemRequestDTO createOrderItemRequestDTO
        = makeCreateOrderItemRequestDTO();

    OrderItem orderItem = makeOrderItem(createOrderItemRequestDTO);
    Long userId = createOrderItemRequestDTO.getUserId();
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
      cartService.addOrderItem(createOrderItemRequestDTO);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_DUPLICATE_MENU);

    assertThat(cartService.isEmptyCart(storeId))
        .isFalse();

    verify(cartDAO, times(0))
        .addOrderItem(orderItem, userId, storeId);
  }


  @Test
  @DisplayName("장바구니 메뉴 조회 성공")
  void readSuccess() {
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
        = cartService.read(userId);

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
  void readFail1() {
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
          = cartService.read(userId);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(CartService.EXCEPTION_EMPTY_CART);

    verify(storeService, times(0))
        .readOneById(storeId);
  }

}

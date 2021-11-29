package com.enjoydelivery.service;

import com.enjoydelivery.dao.CartDAO;
import com.enjoydelivery.dto.cart.request.CreateOrderItemRequestDTO;
import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  public static final String EXCEPTION_INVALID_STORE_ID =
      "유효하지 않은 가게 ID입니다.";
  public static final String EXCEPTION_DUPLICATE_MENU =
      "중복된 메뉴입니다.";
  public static final String EXCEPTION_EMPTY_CART =
      "비어있는 장바구니입니다.";
  private final StoreService storeService;
  private final CartDAO cartDAO;

  public void addOrderItem(CreateOrderItemRequestDTO orderItemCommand) {
    OrderItem orderItem = orderItemCommand.toEntity();
    Long storeId = orderItemCommand.getStoreId();
    Long userId = orderItemCommand.getUserId();
    Long menuId = orderItem.getMenu().getId();

    Long findStoreId = cartDAO.findStoreIdByUserId(userId);

    if (isEmptyCart(findStoreId)) {
      cartDAO.addOrderItem(orderItem, userId, storeId);
      return;
    }

    if (!isSameStore(storeId, findStoreId)) {
      throw new RuntimeException(EXCEPTION_INVALID_STORE_ID);
    }

    if (cartDAO.existOrderItem(menuId, userId)) {
      throw new RuntimeException(EXCEPTION_DUPLICATE_MENU);
    }

    cartDAO.addOrderItem(orderItem, userId, storeId);

  }

  public boolean isSameStore(Long storeId, Long findStoreId) {
    return storeId == findStoreId;
  }

  public boolean isEmptyCart(Long storeId) {
    return storeId == 0;
  }

  public void deleteOneOrderItem(Long menuId, Long userId) {
    cartDAO.deleteOneOrderItem(menuId, userId);
  }

  public void deleteAll(Long userId) {
    cartDAO.deleteAll(userId);
  }

  public ReadCartResponseDTO read(Long userId) {
    List<OrderItem> orderItems = cartDAO.findAllByUserId(userId);
    Long storeId = cartDAO.findStoreIdByUserId(userId);

    if (isEmptyCart(storeId)) {
      throw new RuntimeException(EXCEPTION_EMPTY_CART);
    }

    Store store = storeService.readOneById(storeId);

    ReadCartResponseDTO readCartResponseDTO = new ReadCartResponseDTO(
        storeId,
        store.getName(),
        store.getDeliveryCost(),
        orderItems);

    return readCartResponseDTO;
  }
}

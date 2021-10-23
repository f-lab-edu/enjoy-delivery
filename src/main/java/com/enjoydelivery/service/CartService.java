package com.enjoydelivery.service;

import com.enjoydelivery.dao.CartDAO;
import com.enjoydelivery.dto.cart.request.CreateOrderItemCommand;
import com.enjoydelivery.dto.cart.reseponse.ReadCartCommand;
import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.entity.Store;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final StoreService storeService;
  private final CartDAO cartDAO;

  public void addOrderItem(CreateOrderItemCommand orderItemCommand) {
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
      throw new RuntimeException("같은 가게만 넣을 수 있음");
    }

    if (cartDAO.existOrderItem(menuId, userId)) {
      throw new RuntimeException("똑같은 메뉴를 추가할 경우 장바구니에 넣을 수 없음");
    }

    cartDAO.addOrderItem(orderItem, userId, storeId);

  }

  private boolean isSameStore(Long storeId, Long findStoreId) {
    return storeId == findStoreId;
  }

  private boolean isEmptyCart(Long storeId) {
    return storeId == 0;
  }

  public void deleteOneOrderItem(Long menuId, Long userId) {
    cartDAO.deleteOneOrderItem(menuId, userId);
  }

  public void deleteAll(Long userId) {
    cartDAO.deleteAll(userId);
  }

  public ReadCartCommand read(Long userId) {
    List<OrderItem> orderItems = cartDAO.findAllByUserId(userId);
    Long storeId = cartDAO.findStoreIdByUserId(userId);
    if (isEmptyCart(storeId)) {
      throw new RuntimeException("장바구니가 비어있습니다.");
    }

    Store store = storeService.readOneById(storeId);

    ReadCartCommand readCartCommand = new ReadCartCommand(
        storeId,
        store.getName(),
        store.getDeliveryCost(),
        orderItems);

    return readCartCommand;
  }
}

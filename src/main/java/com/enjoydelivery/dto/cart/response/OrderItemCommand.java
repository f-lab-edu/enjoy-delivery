package com.enjoydelivery.dto.cart.response;

import com.enjoydelivery.dto.store.response.ReadMenuCommand;
import com.enjoydelivery.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCommand {

  private ReadMenuCommand menuCommand;
  private int count;

  public OrderItemCommand(OrderItem orderItem) {
    this.menuCommand = new ReadMenuCommand(orderItem.getMenu());
    this.count = orderItem.getCount();
  }
}
package com.enjoydelivery.dto.cart.response;

import com.enjoydelivery.dto.store.response.ReadMenuResponseDTO;
import com.enjoydelivery.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

  private ReadMenuResponseDTO menuCommand;
  private int count;

  public OrderItemResponseDTO(OrderItem orderItem) {
    this.menuCommand = new ReadMenuResponseDTO(orderItem.getMenu());
    this.count = orderItem.getCount();
  }
}
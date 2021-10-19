package com.enjoydelivery.dto.cart.response;


import com.enjoydelivery.entity.OrderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadCartCommand {

  private Long storeId;
  private String storeName;
  private int deliveryCost;
  private List<OrderItemCommand> orderItemCommands;

  public ReadCartCommand(Long storeId, String storeName, int deliveryCost, List<OrderItem> orderItems) {
    this.storeId = storeId;
    this.storeName = storeName;
    this.deliveryCost = deliveryCost;
    this.orderItemCommands = orderItems.stream()
        .map(orderItem -> new OrderItemCommand(orderItem))
        .collect(Collectors.toList());
  }
}
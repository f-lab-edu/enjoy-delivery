package com.enjoydelivery.dto.cart.response;


import com.enjoydelivery.entity.OrderItem;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadCartResponseDTO {

  private Long storeId;
  private String storeName;
  private int deliveryCost;
  private List<OrderItemResponseDTO> orderItemResponseDTOS;

  public ReadCartResponseDTO(Long storeId, String storeName, int deliveryCost, List<OrderItem> orderItems) {
    this.storeId = storeId;
    this.storeName = storeName;
    this.deliveryCost = deliveryCost;
    this.orderItemResponseDTOS = orderItems.stream()
        .map(orderItem -> new OrderItemResponseDTO(orderItem))
        .collect(Collectors.toList());
  }
}
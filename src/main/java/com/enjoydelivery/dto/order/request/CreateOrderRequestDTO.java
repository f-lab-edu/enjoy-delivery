package com.enjoydelivery.dto.order.request;

import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderState;
import com.enjoydelivery.entity.Pay;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestDTO {

  @NotNull
  private String address;

  @NotNull
  private String phoneNumber;

  @NotNull
  private String description;

  @NotNull
  private PayRequestDTO payRequestDTO;

  public Pay createPayEntity() {
    return payRequestDTO.createEntity();
  }

  public Order createOrderEntity() {
    /*/*[ Order : class ]
createdAt : LocalDateTime
address : String
description : String
orderState : OrderState
orderItems : List<OrderItem>
*/
    return Order.builder()
        .createdAt(LocalDateTime.now())
        .address(this.address)
        .description(this.description)
        .orderState(OrderState.주문접수)
        .build();
  }
}

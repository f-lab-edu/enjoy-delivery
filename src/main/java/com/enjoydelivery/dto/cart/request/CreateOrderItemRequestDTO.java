package com.enjoydelivery.dto.cart.request;

import com.enjoydelivery.dto.store.response.ReadMenuResponseDTO;
import com.enjoydelivery.entity.OrderItem;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemRequestDTO {

  @NotNull
  private Long userId;
  @NotNull
  private Long storeId;
  @NotNull
  private ReadMenuResponseDTO readMenuResponseDTO;
  @NotNull
  private int count;

  public OrderItem toEntity() {
    return OrderItem.builder()
        .menu(readMenuResponseDTO.toEntity())
        .count(count)
        .build();
  }
}
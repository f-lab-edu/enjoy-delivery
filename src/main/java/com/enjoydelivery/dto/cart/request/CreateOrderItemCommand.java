package com.enjoydelivery.dto.cart.request;

import com.enjoydelivery.dto.store.response.ReadMenuCommand;
import com.enjoydelivery.entity.OrderItem;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemCommand {

  @NotNull
  private Long userId;
  @NotNull
  private Long storeId;
  @NotNull
  private ReadMenuCommand readMenuCommand;
  @NotNull
  private int count;

  public OrderItem toEntity() {
    return OrderItem.builder()
        .menu(readMenuCommand.toEntity())
        .count(count)
        .build();
  }
}
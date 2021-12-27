package com.enjoydelivery.dto.order.response;

import com.enjoydelivery.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
  private Long orderItemId;
  private String menuName;
  private int count;

  public static OrderItemDTO create(OrderItem o) {
    return OrderItemDTO.builder()
        .orderItemId(o.getId())
        .menuName(o.getMenu().getName())
        .count(o.getCount())
        .build();
  }

  public String createNotificationBody() {
    String str = "OrderItemDTO{" +
        "orderItemId=" + orderItemId +
        ", menuName='" + menuName + '\'' +
        ", count=" + count +
        '}';

    return str;
  }
}

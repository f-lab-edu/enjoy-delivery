package com.enjoydelivery.dto.order.response;

import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendOrderMessageDTO {

  private Long order_id;
  private LocalDateTime createdAt;
  private String address;
  private String description;
  private PayDTO pay;
  private List<OrderItemDTO> orderItems;

  public static SendOrderMessageDTO create(Order order) {
    List<OrderItemDTO> orders = order.getOrderItems()
        .stream()
        .map(o -> OrderItemDTO.create(o))
        .collect(Collectors.toList());

    return SendOrderMessageDTO.builder()
        .order_id(order.getId())
        .createdAt(order.getCreatedAt())
        .address(order.getAddress())
        .description(order.getDescription())
        .pay(PayDTO.create(order.getPay()))
        .orderItems(orders)
        .build();
  }

  public String createNotificationBody() {
    String str = "SendOrderMessageDTO{" +
        "order_id=" + order_id +
        ", createdAt=" + createdAt +
        ", address='" + address + '\'' +
        ", description='" + description + '\'' +
        ", pay=" + pay.createNotificationBody() +
        ", orderItems=" + createNotificationBody(orderItems) +
        '}';
    return str;
  }

  private String createNotificationBody(List<OrderItemDTO> orderItems) {
    return orderItems.stream()
        .map(o -> o.createNotificationBody())
        .collect(Collectors.joining(",","{","}"));
  }

}

package com.enjoydelivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.enjoydelivery.entity.Order;
import com.enjoydelivery.entity.OrderState;
import com.enjoydelivery.exception.NoEntityException;
import com.enjoydelivery.exception.OrderFailException;
import com.enjoydelivery.repository.OrderRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderCancelerTest {


  @Mock
  private OrderRepository repository;

  @Test
  public void sut_changes_state_of_order_to_주문취소() {
    // Arrange
    long orderId = 1;
    Order order = Order.builder()
        .id(orderId)
        .orderState(OrderState.주문접수)
        .build();

    doReturn(Optional.of(order))
        .when(repository)
        .findById(orderId);

    OrderCanceler sut = new OrderCanceler(repository);

    // Act
    sut.cancelOrder(orderId);

    // Assert
    assertThat(order.getOrderState())
        .isEqualTo(OrderState.주문취소);
  }

  @Test
  public void sut_throws_no_entity_exception_when_order_does_not_exist() {
    // Arrange
    OrderCanceler sut = new OrderCanceler(repository);
    long orderId = 1;

    doReturn(Optional.empty())
        .when(repository)
        .findById(orderId);

    // Act & Assert
    assertThatThrownBy(() -> {
      sut.cancelOrder(orderId);
    }).isInstanceOf(NoEntityException.class);
  }

  @Test
  public void sut_fails_if_order_already_canceled() {
    // Arrange
    long orderId = 1;
    Order order = Order.builder()
        .id(orderId)
        .orderState(OrderState.주문취소)
        .build();

    doReturn(Optional.of(order))
        .when(repository)
        .findById(orderId);

    OrderCanceler sut = new OrderCanceler(repository);

    // Act , Assert
    assertThatThrownBy(() -> {
      sut.cancelOrder(orderId);
    }).isInstanceOf(OrderFailException.class);

  }

  @Test
  public void sut_fails_if_order_already_completed() {
    // Arrange
    long orderId = 1;
    Order order = Order.builder()
        .id(orderId)
        .orderState(OrderState.주문완료)
        .build();

    doReturn(Optional.of(order))
        .when(repository)
        .findById(orderId);

    OrderCanceler sut = new OrderCanceler(repository);

    // Act , Assert
    assertThatThrownBy(() -> {
      sut.cancelOrder(orderId);
    }).isInstanceOf(OrderFailException.class);
  }
}
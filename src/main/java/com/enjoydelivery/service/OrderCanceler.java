package com.enjoydelivery.service;

import com.enjoydelivery.entity.Order;
import com.enjoydelivery.exception.NoEntityException;
import com.enjoydelivery.exception.OrderFailException;
import com.enjoydelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCanceler {

  private final OrderRepository repository;

  public void cancelOrder(long orderId) {
    Order order = repository.findById(orderId)
        .orElseThrow(NoEntityException::new);

    if (order.isCanceled() || order.isCompleted()) {
      throw new OrderFailException();
    }

    order.cancel();
  }
}
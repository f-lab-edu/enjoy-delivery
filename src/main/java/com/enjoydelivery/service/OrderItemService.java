package com.enjoydelivery.service;

import com.enjoydelivery.entity.OrderItem;
import com.enjoydelivery.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

  private final OrderItemRepository repository;

  public void saveAll(List<OrderItem> orderItems) {
    repository.saveAll(orderItems);
  }
}

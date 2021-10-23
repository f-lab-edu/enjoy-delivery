package com.enjoydelivery.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @Column(name = "order_id")
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
  private String address;
  private String description;

  @Column(name = "order_state")
  @Enumerated(EnumType.STRING)
  private OrderState orderState;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="pay_id")
  private Pay pay;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems;
}

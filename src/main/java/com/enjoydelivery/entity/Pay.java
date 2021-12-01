package com.enjoydelivery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pay {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pay_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "pay_type")
  private PayType payType;

  @Column(name = "origin_cost")
  private int originCost;

  @Column(name = "discount_cost")
  private int discountCost;

  @Enumerated(EnumType.STRING)
  @Column(name = "pay_state")
  private PayState payState;
}

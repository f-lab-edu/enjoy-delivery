package com.enjoydelivery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue
  private Long id;

  private String uid;

  @Column(name = "hashed_password")
  private String hashedPassword;

  private String email;

  private String name;

  @Column(name = "phone_number")
  private String phoneNumber;

  private String address;

  @Column(name = "user_type")
  @Enumerated(EnumType.STRING)
  private UserType userType;
}

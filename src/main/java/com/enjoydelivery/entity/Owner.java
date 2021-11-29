package com.enjoydelivery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Owner {

  @Id
  @Column(name = "owner_id")
  @GeneratedValue
  private Long id;
  private String uid;

  @Column(name = "hashed_password")
  private String hashedPassword;
  private String email;
  private String name;

  @Column(name = "phone_number")
  private String phoneNumber;

}

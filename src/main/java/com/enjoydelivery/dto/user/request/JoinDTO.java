package com.enjoydelivery.dto.user.request;

import com.enjoydelivery.entity.User;
import com.enjoydelivery.entity.UserType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinDTO {

  @NotNull
  String uid;

  @NotNull
  String password;

  @NotNull
  String email;

  @NotNull
  String name;

  @NotNull
  String phoneNumber;

  @NotNull
  String address;

  @NotNull
  UserType userType;

  public User createEntity() {
    return User.builder()
        .address(this.address)
        .name(this.name)
        .email(this.email)
        .phoneNumber(this.phoneNumber)
        .userType(this.userType)
        .uid(this.uid)
        .userType(this.userType)
        .hashedPassword(this.password)
        .build();
  }
}

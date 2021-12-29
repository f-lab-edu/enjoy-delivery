package com.enjoydelivery.dto.user.response;

import com.enjoydelivery.entity.User;
import com.enjoydelivery.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadUserDTO {

  private String uid;

  private String email;

  private String name;

  private String phoneNumber;

  private String address;

  private UserType userType;

  public ReadUserDTO(User user) {
    this.uid = user.getUid();
    this.email = user.getEmail();
    this.name = user.getName();
    this.phoneNumber = user.getPhoneNumber();
    this.address = user.getAddress();
    this.userType = user.getUserType();
  }
}


package com.enjoydelivery.dto.user.request;

import com.enjoydelivery.entity.UserType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

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
}

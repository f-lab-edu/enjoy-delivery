package com.enjoydelivery.dto.user.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  @NotNull
  String uid;

  @NotNull
  String password;
}

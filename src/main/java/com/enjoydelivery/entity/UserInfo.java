package com.enjoydelivery.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo implements Serializable {

  private Long id;

  private UserType userType;

}

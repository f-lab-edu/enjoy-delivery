package com.enjoydelivery.service;

import com.enjoydelivery.entity.UserInfo;

public interface LoginService {
  void loginUser(UserInfo userInfo);

  UserInfo getCurrentUserInfo();

}

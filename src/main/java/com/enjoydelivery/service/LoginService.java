package com.enjoydelivery.service;

import com.enjoydelivery.entity.UserInfo;

public interface LoginService {
  void loginUser(Long userId, UserInfo userInfo);

  UserInfo getCurrentUserInfo(Long userId);

}

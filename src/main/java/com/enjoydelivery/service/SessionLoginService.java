package com.enjoydelivery.service;

import com.enjoydelivery.entity.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

  private static final String USER = "user";
  private final HttpSession session;
  private final RedisTemplate redisTemplate;

  @Override
  public void loginUser(UserInfo userInfo) {
    session.setAttribute(USER, userInfo);
  }

  @Override
  public UserInfo getCurrentUserInfo() {
    return (UserInfo) session.getAttribute(USER);
  }
}
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

  private final HttpSession session;
  private final RedisTemplate redisTemplate;

  @Override
  public void loginUser(Long userId, UserInfo userInfo) {
    session.setAttribute(String.valueOf(userId), userInfo);
  }

  @Override
  public UserInfo getCurrentUserInfo(Long userId) {
    return (UserInfo) session.getAttribute(String.valueOf(userId));
  }
}
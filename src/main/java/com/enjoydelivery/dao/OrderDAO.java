package com.enjoydelivery.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDAO {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  public void addOwnerToken(Long ownerId, String token) {
    redisTemplate.opsForValue()
        .set(String.valueOf(ownerId), token);
  }

  public String getOwnerToken(Long ownerId) {
    Object obj = redisTemplate.opsForValue()
        .get(ownerId);
    return objectMapper.convertValue(obj, String.class);
  }

}

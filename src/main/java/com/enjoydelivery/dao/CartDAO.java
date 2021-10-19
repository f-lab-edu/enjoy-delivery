package com.enjoydelivery.dao;

import com.enjoydelivery.entity.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CartDAO {

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  private String generateKey(Long userId) {
    return userId + "";
  }
  private String generateHashKey(Long userId, Long storeId) {
    return userId + ":" + storeId;
  }

  private String getHashKey(Long userId) {
    String key = generateKey(userId);
    Object object = redisTemplate.opsForValue().get(key);
    Long storeId = objectMapper.convertValue(object, Long.class);
    return generateHashKey(userId, storeId);
  }

  public void addOrderItem(OrderItem orderItem, Long userId, Long storeId) {
    String key = generateKey(userId);
    String hashKey = generateHashKey(userId, storeId);

    redisTemplate.opsForValue().set(key, storeId);
    redisTemplate.opsForHash().put(hashKey, orderItem.getMenu().getId()+"", orderItem);

  }

  public void deleteAll(Long userId) {
    String key = generateKey(userId);
    String hashKey = getHashKey(userId);
    redisTemplate.delete(hashKey);
    redisTemplate.delete(key);
  }


  public void deleteOneOrderItem(Long menuId, Long userId) {
    String hashKey = getHashKey(userId);
    String key = generateKey(userId);
    redisTemplate.opsForHash().delete(hashKey, menuId+"");
    if (redisTemplate.opsForHash().size(hashKey) == 0) {
      redisTemplate.delete(hashKey);
      redisTemplate.delete(key);
    }

  }

  public List<OrderItem> findAllByUserId(Long userId) {
    String key = getHashKey(userId);

    List<Object> values = redisTemplate.opsForHash().values(key);
    return values.stream()
        .map(o -> objectMapper.convertValue(o, OrderItem.class))
        .collect(Collectors.toList());
  }

  public Long findStoreIdByUserId(Long userId) {
    String key = generateKey(userId);
    Object value = redisTemplate.opsForValue().get(key);
    return Optional.ofNullable(value)
        .map(o -> objectMapper.convertValue(o, Long.class))
        .orElse(0L);
  }

  public boolean existOrderItem(Long menuId, Long userId) {
    String key = getHashKey(userId);
    return redisTemplate.opsForHash().hasKey(key, menuId+"");
  }
}

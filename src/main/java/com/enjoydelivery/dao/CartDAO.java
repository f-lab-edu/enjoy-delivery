package com.enjoydelivery.dao;

import com.enjoydelivery.entity.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ScanOptions;
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

    List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();
        operations.opsForValue().set(key, storeId);
        operations.opsForHash().put(hashKey, orderItem.getMenu().getId()+"", orderItem);
        return operations.exec();
      }
    });
  }


  public void deleteAll(Long userId) {
    String key = generateKey(userId);
    String hashKey = getHashKey(userId);

    List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();

        ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
        Cursor cursor = operations.opsForHash().scan(hashKey, scanOptions);
        String[] fields = new String[operations.opsForHash().size(hashKey).intValue()];
        int i = 0;
        while(cursor.hasNext()) {
          Object o = cursor.next();
          String str = objectMapper.convertValue(o, String.class);
          fields[i++] = str;
        }
        operations.opsForHash().delete(hashKey, fields);

        operations.delete(hashKey);
        operations.delete(key);

        return operations.exec();
      }
    });
  }


 public void deleteOneOrderItem(Long menuId, Long userId) {
    String hashKey = getHashKey(userId);
    String key = generateKey(userId);

   List<Object> txResults = redisTemplate.execute(new SessionCallback<List<Object>>() {
     public List<Object> execute(RedisOperations operations) throws DataAccessException {
       operations.multi();

       operations.opsForHash().delete(hashKey, menuId+"");
       if (operations.opsForHash().size(hashKey) == 0) {
         operations.delete(hashKey);
         operations.delete(key);
       }

       return operations.exec();
     }
   });

  }

  public List<OrderItem> findAllByUserId(Long userId) {

    String key = getHashKey(userId);
    List<OrderItem> result = new ArrayList<>();
    ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
    Cursor cursor = redisTemplate.opsForHash().scan(key, scanOptions);
    while(cursor.hasNext()) {
      Object o = cursor.next();
      OrderItem oi = objectMapper.convertValue(o, OrderItem.class);
      result.add(oi);
    }

    return result;
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

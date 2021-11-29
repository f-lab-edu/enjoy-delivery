package com.enjoydelivery.dao;

import com.enjoydelivery.entity.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
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

    redisTemplate.execute(new SessionCallback<List<Object>>() {
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

    redisTemplate.execute(new SessionCallback<List<Object>>() {
      public List<Object> execute(RedisOperations operations) throws DataAccessException {
        operations.multi();

        Set<Object> fields = operations.opsForHash().keys(hashKey);
        for(Object o : fields) {
          operations.opsForHash().delete(hashKey, o);
        }
        
        operations.delete(hashKey);
        operations.delete(key);

        return operations.exec();
      }
    });
  }


  public void deleteOneOrderItem(Long menuId, Long userId) {
    String hashKey = getHashKey(userId);
    String key = generateKey(userId);
    Long size = redisTemplate.opsForHash().size(hashKey);

   redisTemplate.execute(new SessionCallback<List<Object>>() {
     public List<Object> execute(RedisOperations operations) throws DataAccessException {
       operations.multi();

       operations.opsForHash().delete(hashKey, menuId+"");
       if (size - 1 == 0) {
         operations.delete(hashKey);
         operations.delete(key);
       }

       return operations.exec();
     }
   });

  }

  public List<OrderItem> findAllByUserId(Long userId) {

    RedisSerializer keySerializer = redisTemplate.getKeySerializer();
    RedisSerializer hashValueSerialier = redisTemplate.getHashValueSerializer();
    String key = getHashKey(userId);
    List<Object> result = new ArrayList<>();

    redisTemplate.execute(
        new RedisCallback<Object>() {
          public Object doInRedis(RedisConnection connection) throws DataAccessException {
            ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(100).build();
            Cursor<Entry<byte[], byte[]>> cursor = connection.hashCommands().hScan(
                keySerializer.serialize(key), scanOptions);

            while(cursor.hasNext()) {
              Entry<byte[], byte[]> entry = cursor.next();

              result.add(hashValueSerialier.deserialize(entry.getValue()));
            }
            return result;
          }
        });


    return Optional.ofNullable(result)
        .orElse(new ArrayList<>())
        .stream()
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

package com.enjoydelivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  @Value("${spring.redis.cache.host}")
  private String cacheHost;

  @Value("${spring.redis.session.host}")
  private String sessionHost;

  @Value("${spring.redis.cache.port}")
  private int cachePost;

  @Value("${spring.redis.session.port}")
  private int sessionPort;

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModules(new JavaTimeModule(), new Jdk8Module());
    return mapper;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {

    GenericJackson2JsonRedisSerializer serializer =
        new GenericJackson2JsonRedisSerializer(objectMapper());

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisCacheConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(serializer);
    return redisTemplate;
  }

  @Bean
  public RedisCacheManager redisCacheManager() {

    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
        .defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()))
        )
        .entryTtl(Duration.ofDays(1L));

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisCacheConnectionFactory())
        .cacheDefaults(redisCacheConfiguration)
        .build();
  }



  @Bean({"redisConnectionFactory", "redisSessionConnectionFactory"})
  public RedisConnectionFactory redisSessionConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(sessionHost);
    redisStandaloneConfiguration.setPort(sessionPort);

    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
        redisStandaloneConfiguration);

    return lettuceConnectionFactory;
  }

  @Bean
  public RedisConnectionFactory redisCacheConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(cacheHost);
    redisStandaloneConfiguration.setPort(cachePost);
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
        redisStandaloneConfiguration);

    return lettuceConnectionFactory;
  }

}

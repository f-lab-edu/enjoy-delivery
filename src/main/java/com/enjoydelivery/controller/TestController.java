package com.enjoydelivery.controller;

import com.enjoydelivery.entity.TestEntity.Num;
import com.enjoydelivery.entity.TestEntity.Num2;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

  private final HttpSession httpSession;
  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;
  private static final String key = "number";
  private static final String key2 = "numberObject";

  @PostMapping("/session/{number}")
  public ResponseEntity<Long> sessionTest(@PathVariable Long number) {
    httpSession.setAttribute(key, number);
    httpSession.setAttribute(key2, new Num(150));
    return new ResponseEntity(number, HttpStatus.OK);
  }

  @GetMapping("/session")
  public ResponseEntity<Num> sessionTest() {
    System.out.println("session number Long test : " + httpSession.getAttribute(key));
    System.out.println("session number Object test : " + httpSession.getAttribute(key2));

    Num numm = (Num) httpSession.getAttribute(key2);

    return new ResponseEntity<>(numm, HttpStatus.OK);
  }


  @PostMapping("/cache/{number}")
  public ResponseEntity<Num2> cacheTest(@PathVariable Long number) {

    redisTemplate.opsForValue().set(key, number);

    System.out.println("cache number Long test : " + redisTemplate.opsForValue().get(key));
    Num2 input = new Num2(170);
    redisTemplate.opsForValue().set(key2, input);
    Object object = redisTemplate.opsForValue().get(key2);
    Num2 nummm = objectMapper.convertValue(object, Num2.class);
    System.out.println("cache number Obj test : " + nummm);

    return new ResponseEntity<>(nummm, HttpStatus.OK);
  }
}

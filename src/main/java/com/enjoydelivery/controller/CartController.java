package com.enjoydelivery.controller;

import com.enjoydelivery.dto.cart.request.CreateOrderItemRequestDTO;
import com.enjoydelivery.dto.cart.response.ReadCartResponseDTO;
import com.enjoydelivery.entity.UserInfo;
import com.enjoydelivery.entity.UserType;
import com.enjoydelivery.exception.ForbiddenException;
import com.enjoydelivery.service.CartService;
import com.enjoydelivery.service.LoginService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

  private final CartService cartService;
  private final LoginService loginService;

  @PostMapping
  public ResponseEntity addOrderItem(@RequestBody @Valid CreateOrderItemRequestDTO orderItemCommand) {

    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();
    if (!userInfo.getUserType().equals(UserType.USER)) {
      throw new ForbiddenException();
    }
    cartService.addOrderItem(orderItemCommand, userId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity read() {
    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();
    if (!userInfo.getUserType().equals(UserType.USER)) {
      throw new ForbiddenException();
    }
    ReadCartResponseDTO readCartResponseDTO = cartService.read(userId);
    return new ResponseEntity(readCartResponseDTO, HttpStatus.OK);
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity deleteOneOrderItem(@PathVariable("menuId") @Valid Long menuId) {
    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();
    if (!userInfo.getUserType().equals(UserType.USER)) {
      throw new ForbiddenException();
    }
    cartService.deleteOneOrderItem(menuId, userId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping()
  public ResponseEntity deleteAll() {
    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();
    if (!userInfo.getUserType().equals(UserType.USER)) {
      throw new ForbiddenException();
    }
    cartService.deleteAll(userId);
    return new ResponseEntity(HttpStatus.OK);
  }

}

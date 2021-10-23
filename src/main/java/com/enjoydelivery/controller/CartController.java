package com.enjoydelivery.controller;

import com.enjoydelivery.dto.cart.request.CreateOrderItemCommand;
import com.enjoydelivery.dto.cart.reseponse.ReadCartCommand;
import com.enjoydelivery.service.CartService;
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

  @PostMapping
  public ResponseEntity addOrderItem(@RequestBody @Valid CreateOrderItemCommand orderItemCommand) {
    cartService.addOrderItem(orderItemCommand);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity read(@PathVariable("userId") @Valid Long userId) {
    ReadCartCommand readCartCommand = cartService.read(userId);
    return new ResponseEntity(readCartCommand, HttpStatus.OK);
  }

  @DeleteMapping("/{menuId}/{userId}")
  public ResponseEntity deleteOneOrderItem(@PathVariable("menuId") @Valid Long menuId,
      @PathVariable("userId") @Valid Long userId) {
    cartService.deleteOneOrderItem(menuId, userId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity deleteAll(@PathVariable("userId") @Valid Long userId) {
    cartService.deleteAll(userId);
    return new ResponseEntity(HttpStatus.OK);
  }

}

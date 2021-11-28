package com.enjoydelivery.controller;

import com.enjoydelivery.dto.user.request.JoinDTO;
import com.enjoydelivery.dto.user.request.LoginDTO;
import com.enjoydelivery.dto.user.request.UpdateUserDTO;
import com.enjoydelivery.dto.user.response.ReadUserDTO;
import com.enjoydelivery.entity.User;
import com.enjoydelivery.entity.UserInfo;
import com.enjoydelivery.entity.UserType;
import com.enjoydelivery.service.LoginService;
import com.enjoydelivery.service.UserService;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final LoginService loginService;

  @PostMapping("/join")
  public ResponseEntity join(@RequestBody @Valid JoinDTO joinDTO) {
    userService.join(joinDTO);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity login(
      @RequestBody @Valid LoginDTO loginDTO) {
    userService.login(
        loginDTO.getUid(),
        loginDTO.getPassword());

    return new ResponseEntity(HttpStatus.OK);
  }

  @PutMapping("/edit")
  public ResponseEntity update(
      @RequestBody @Valid UpdateUserDTO updateUserDTO) {

    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();
    userService.update(userId, updateUserDTO);

    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<ReadUserDTO> read() {

    UserInfo userInfo = loginService.getCurrentUserInfo();
    Long userId = userInfo.getId();

    User user = userService.readOneById(userId);

    ReadUserDTO readUserDTO = new ReadUserDTO(user);
    return new ResponseEntity<>(readUserDTO, HttpStatus.OK);
  }

  @GetMapping("/test")
  public ResponseEntity<UserInfo> sessionTest() {
    UserInfo userInfo = loginService.getCurrentUserInfo();

    return new ResponseEntity(userInfo, HttpStatus.OK);

  }

}

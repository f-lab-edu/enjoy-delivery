package com.enjoydelivery.service;

import com.enjoydelivery.dto.user.request.JoinDTO;
import com.enjoydelivery.dto.user.request.UpdateUserDTO;
import com.enjoydelivery.entity.User;
import com.enjoydelivery.entity.UserInfo;
import com.enjoydelivery.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final LoginService loginService;

  public void join(JoinDTO joinDTO) {

    if (userRepository.existsByName(joinDTO.getName())) {
      throw new RuntimeException();
    }

    User user = joinDTO.createEntity();
    user.changeHashedPassword(passwordEncoder);

    userRepository.save(user);
  }

  public void login(String uid, String password) {

    User findUser = readOneByUid(uid);

    validatePassword(password, findUser.getHashedPassword());

    UserInfo userInfo = UserInfo.builder()
        .id(findUser.getId())
        .userType(findUser.getUserType())
        .createAt(LocalDateTime.now())
        .build();

    loginService.loginUser(userInfo);

  }

  public void validatePassword(String originPassword, String newPassword) {
    if (!passwordEncoder.matches(originPassword, newPassword)) {
      throw new RuntimeException();
    }
  }

  public User readOneByUid(String uid) {
    return userRepository.findByUid(uid)
        .orElseThrow(() -> new RuntimeException());
  }

  public User readOneById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException());
  }

  public void update(Long id, UpdateUserDTO updateUserDTO) {
    User user = readOneById(id);
    user.update(updateUserDTO);
  }
}

package com.enjoydelivery.entity;

import com.enjoydelivery.dto.user.request.UpdateUserDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

  @Id
  @Column(name = "user_id")
  @GeneratedValue
  private Long id;

  private String uid;

  @Column(name = "hashed_password")
  private String hashedPassword;

  private String email;

  private String name;

  @Column(name = "phone_number")
  private String phoneNumber;

  private String address;

  @Column(name = "user_type")
  @Enumerated(EnumType.STRING)
  private UserType userType;

  public void changeHashedPassword(PasswordEncoder passwordEncoder) {
    String pw = passwordEncoder.encode(this.hashedPassword);
    if (pw == null || pw.isBlank() || pw.isEmpty()) {
      throw new RuntimeException();
    }
    this.hashedPassword = pw;
  }

  public void update(UpdateUserDTO updateUserDTO) {
    this.address = updateUserDTO.getAddress();
    this.userType = updateUserDTO.getUserType();
    this.name = updateUserDTO.getName();
    this.phoneNumber = updateUserDTO.getPhoneNumber();
    this.email = updateUserDTO.getEmail();
  }
}

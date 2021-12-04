package com.enjoydelivery.repository;


import com.enjoydelivery.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByName(String name);

  Optional<User> findByUid(String uid);
}

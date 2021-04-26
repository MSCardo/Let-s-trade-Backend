package com.greenfoxacademy.tradebackend.repository;


import com.greenfoxacademy.tradebackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository
    extends JpaRepository<User, Long> {

  Optional<User> findUserByUsername(String username);

  @Query(value = "SELECT username FROM user", nativeQuery = true)
  List<String> findAllUsername();


  Optional<User> findByEmail(String email);

  @Transactional
  @Modifying
  @Query("UPDATE User a " +
      "SET a.enabled = TRUE WHERE a.email = ?1")
  int enableAppUser(String email);

}

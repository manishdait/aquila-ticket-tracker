package com.example.latte_api.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailOrFirstname(String email, String firstname);
  Optional<User> findByFirstname(String firstname);
  Optional<User> findByEmail(String email);
}

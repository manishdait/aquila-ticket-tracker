package com.example.latte_api.utils;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.latte_api.user.User;
import com.example.latte_api.user.UserRepository;
import com.example.latte_api.user.role.Role;
import com.example.latte_api.user.role.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultSetup {
  private final RoleRepository roleRepository; 
  private final UserRepository userRepository;

  private final PasswordEncoder encoder;

  @PostConstruct
  public void init() {
    File dir = new File(".cred");
    if(!dir.exists()) {
      dir.mkdir();
    }

    File cred = new File(".cred/cred.txt");
    try {
      if (!cred.exists()) {
        cred.createNewFile();
        String password = UUID.randomUUID().toString() + LocalTime.now().hashCode();
  
        FileWriter fileWriter = new FileWriter(cred);
        fileWriter.append(password);
        fileWriter.close();
        
        cred.setReadOnly();
  
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN").orElseThrow();
        User admin = User.builder()
          .firstname("Admin")
          .email("admin@admin.com")
          .password(encoder.encode(password))
          .role(roleAdmin)
          .build();
  
        userRepository.save(admin);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("Exception occurs during initializing default admin user: {}", e.getMessage());
    }
  }
}

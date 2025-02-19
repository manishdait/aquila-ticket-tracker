package com.example.latte_api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.latte_api.user.User;
import com.example.latte_api.user.UserRepository;
import com.example.latte_api.user.dto.ResetPasswordRequest;
import com.example.latte_api.user.dto.UserDto;
import com.example.latte_api.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserDto resetPassword(ResetPasswordRequest request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    if (!request.updatePassword().equals(request.confirmPassword())) {
      throw new IllegalArgumentException("Update password and Confirm password not match");
    }

    user.setPassword(passwordEncoder.encode(request.confirmPassword()));
    userRepository.save(user);
    return userMapper.mapToUserDto(user);
  } 

  public UserDto resetPassword(ResetPasswordRequest request, String _user) {
    User user = userRepository.findByEmail(_user).orElseThrow();
    
    if (!request.updatePassword().equals(request.confirmPassword())) {
      throw new IllegalArgumentException("Update password and Confirm password not match");
    }

    user.setPassword(passwordEncoder.encode(request.confirmPassword()));
    userRepository.save(user);
    return userMapper.mapToUserDto(user);
  } 
}

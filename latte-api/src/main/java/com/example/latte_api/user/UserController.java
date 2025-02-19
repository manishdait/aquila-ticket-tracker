package com.example.latte_api.user;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.latte_api.security.PasswordService;
import com.example.latte_api.shared.PagedEntity;
import com.example.latte_api.user.dto.ResetPasswordRequest;
import com.example.latte_api.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/latte-api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final PasswordService passwordService;

  @GetMapping()
  public ResponseEntity<PagedEntity<UserDto>> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(page, size));
  }

  @GetMapping("/list")
  public ResponseEntity<PagedEntity<String>> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserList(page, size));
  }

  @GetMapping("/info")
  public ResponseEntity<UserDto> getInfo(Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(authentication));
  }

  @GetMapping("/info/{email}")
  public ResponseEntity<UserDto> getInfoForUser(@PathVariable String email) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(email));
  }

  @PutMapping()
  public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto, authentication));
  }

  @PutMapping("/{email}")
  public ResponseEntity<UserDto> updateUserByEmail(@RequestBody UserDto userDto, @PathVariable String email) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto, email));
  }

  @PatchMapping()
  public ResponseEntity<UserDto> resetUserPassword(@RequestBody ResetPasswordRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(passwordService.resetPassword(request, authentication));
  }

  @PatchMapping("/{email}")
  public ResponseEntity<UserDto> resetUserPassword(@RequestBody ResetPasswordRequest request, @PathVariable String email) {
    return ResponseEntity.status(HttpStatus.OK).body(passwordService.resetPassword(request, email));
  }

  @DeleteMapping("/{email}")
  public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String email) {
    userService.deleteUser(email);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("key", email, "deleted", true));
  }
}

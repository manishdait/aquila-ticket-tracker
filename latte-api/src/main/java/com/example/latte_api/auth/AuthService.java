package com.example.latte_api.auth;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.latte_api.auth.dto.AuthRequest;
import com.example.latte_api.auth.dto.AuthResponse;
import com.example.latte_api.auth.dto.RegistrationRequest;
import com.example.latte_api.security.JwtProvider;
import com.example.latte_api.user.User;
import com.example.latte_api.user.UserRepository;
import com.example.latte_api.user.role.Role;
import com.example.latte_api.user.role.RoleRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  @Transactional
  public void registerUser(RegistrationRequest request) {
    userRepository.findByEmailOrFirstname(request.email(), request.firstname()).ifPresent((user) -> {
      throw new IllegalArgumentException("Duplicate User");
    });

    Role role = roleRepository.findByRole(request.role()).orElseThrow(
      () -> new IllegalArgumentException("Role not exist")
    );

    User user = User.builder()
      .firstname(request.firstname())
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(role)
      .build();

    userRepository.save(user);  
  }

  public AuthResponse authenticateUser(AuthRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    User user = (User) authentication.getPrincipal();
    String role = user.getRole().getRole();

    String accessToken = jwtProvider.generateToken(user.getEmail(), Map.of("roles", role, "firstname", user.getFirstname()));
    String refreshToken = jwtProvider.generateToken(user.getEmail(), Map.of("roles", role, "firstname", user.getFirstname()),  604800);

    return new AuthResponse(user.getEmail(), accessToken, refreshToken, user.getRole().getRole());
  }

  public AuthResponse refreshToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (token == null || !token.startsWith("Bearer ")) {
      throw new RuntimeException("Forbidden access");
    }

    token = token.substring(7);
    String username = jwtProvider.getUsername(token);
    if (username == null) {
      throw new RuntimeException("Forbidden access");
    }

    User userDetails = userRepository.findByEmail(username).orElseThrow();

    if (!jwtProvider.validToken(userDetails, token)) {
      throw new RuntimeException("Forbidden access");
    }

    String role = userDetails.getRole().getRole();
    String accessToken = jwtProvider.generateToken(username, Map.of("roles", role, "firstname", userDetails.getFirstname()));
    return new AuthResponse(userDetails.getUsername(), accessToken, token, role);
  }
}

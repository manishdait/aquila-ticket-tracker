package com.example.latte_api.auth.dto;

public record AuthResponse(String email, String accessToken, String refreshToken, String role) {
  
}

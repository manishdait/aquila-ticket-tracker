package com.example.latte_api.auth.dto;

public record RegistrationRequest (String firstname, String email, String password, String role) {
  
}

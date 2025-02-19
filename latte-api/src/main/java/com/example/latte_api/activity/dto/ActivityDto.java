package com.example.latte_api.activity.dto;

import java.time.Instant;

import com.example.latte_api.activity.enums.ActivityType;

public record ActivityDto(Long id, String message, ActivityType type, String author, Instant createdAt, Instant lastUpdated) {
  
}

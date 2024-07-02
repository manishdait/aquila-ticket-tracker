package io.github.manishdait.aquila.project;

import java.time.Instant;
import java.util.List;

import io.github.manishdait.aquila.users.UserResponse;

public record ProjectResponse (Long id, String code, String name, String description, Integer ticketCount, Instant createdAt, List<UserResponse> teamMembers) {
    
}

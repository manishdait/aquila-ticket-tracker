package io.github.manishdait.aquila.ticket;

import java.time.Instant;
import java.util.List;

import io.github.manishdait.aquila.users.UserResponse;

public record TicketResponse (Long id, String title, String description, Instant createdAt, Instant updatedAt, Priority priority, Status status, UserResponse reportedBy, List<UserResponse> assignees, Long projectId, String projectCode,Integer commentCount) {
    
}

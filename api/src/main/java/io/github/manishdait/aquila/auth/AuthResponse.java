package io.github.manishdait.aquila.auth;

import java.time.Instant;

public record AuthResponse (String username, String role, String authToken, String refreshToken, Instant createdAt, Instant expireAt) {
}

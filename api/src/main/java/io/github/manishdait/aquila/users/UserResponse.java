package io.github.manishdait.aquila.users;

public record UserResponse (String name, String email, String role, boolean enabled) {
    
}

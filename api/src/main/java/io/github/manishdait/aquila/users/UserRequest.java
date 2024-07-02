package io.github.manishdait.aquila.users;

public record UserRequest (String name, String email, String role, String password, boolean enabled) {
 
}

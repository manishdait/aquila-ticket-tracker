package io.github.manishdait.aquila.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.manishdait.aquila.auth.token.referesh.RefereshTokenRequest;
import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.ExceptionResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/aquila-api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public void signUp(@RequestBody SignupRequest request) {
        authService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(authService.login(request));
    }

    @PostMapping("/referesh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefereshTokenRequest request) {
        return ResponseEntity.ok().body(authService.refreshToken(request));
    }

    @DeleteMapping("/logout/{token}")
    public void refreshToken(@PathVariable String token) {
        authService.logout(token);
    }

    @ExceptionHandler(AquilaApiException.class)
    public ResponseEntity<ExceptionResponse> handleException(AquilaApiException e) {
        return ResponseEntity.status(e.getStatus())
        .body(new ExceptionResponse(e.getStatus().value(), e.getTimestamp(), e.getError(), e.getMessage()));
    }
}

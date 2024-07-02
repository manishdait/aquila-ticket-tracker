package io.github.manishdait.aquila.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.ExceptionResponse;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/aquila-api/activate")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AccountActivationController {
    private final AuthService authService;

    @GetMapping("account/{token}")
    public String getMethodName(@PathVariable String token) {
        authService.activateAccount(token);
        return "accountActivation";
    }

    @ExceptionHandler(AquilaApiException.class)
    public ResponseEntity<ExceptionResponse> handleException(AquilaApiException e) {
        return ResponseEntity.status(e.getStatus())
        .body(new ExceptionResponse(e.getStatus().value(), e.getTimestamp(), e.getError(), e.getMessage()));
    }
}

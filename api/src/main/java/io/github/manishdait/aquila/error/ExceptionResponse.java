package io.github.manishdait.aquila.error;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ExceptionResponse (HttpStatus status, Instant timestamp, String error, String message) {
    
}

package io.github.manishdait.aquila.error;

import java.time.Instant;

public record ExceptionResponse (int status, Instant timestamp, String error, String message) {
    
}

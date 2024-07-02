package io.github.manishdait.aquila.error;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public class AquilaApiException extends RuntimeException {
    private HttpStatus status;
    private String error;
    private Instant timestamp;

    public AquilaApiException(HttpStatus status, String error, String message, Instant timestamp) {
        super(message);
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

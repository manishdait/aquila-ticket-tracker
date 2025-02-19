package com.example.latte_api.handler;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, Integer status, String error, String path) {

}

package io.github.manishdait.aquila.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.ExceptionResponse;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/aquila-api/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }
    
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(request));
    }

    @ExceptionHandler(AquilaApiException.class)
    public ResponseEntity<ExceptionResponse> handleException(AquilaApiException e) {
        return ResponseEntity.status(e.getStatus())
        .body(new ExceptionResponse(e.getStatus().value(), e.getTimestamp(), e.getError(), e.getMessage()));
    }
}

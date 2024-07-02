package io.github.manishdait.aquila.comment;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.ExceptionResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/aquila-api/comment")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment (@RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments () {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(id));
    }

    @GetMapping("/by-ticket/{id}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTicket (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByTicket(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser (@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByUser(username));
    }

    @ExceptionHandler(AquilaApiException.class)
    public ResponseEntity<ExceptionResponse> handleException(AquilaApiException e) {
        return ResponseEntity.status(e.getStatus())
        .body(new ExceptionResponse(e.getStatus().value(), e.getTimestamp(), e.getError(), e.getMessage()));
    }
}

package com.example.latte_api.comment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.latte_api.activity.dto.ActivityDto;
import com.example.latte_api.comment.dto.CommentRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/latte-api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @PostMapping()
  public ResponseEntity<ActivityDto> createComment(@RequestBody CommentRequest request, Authentication authentication) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.createComment(request, authentication));
  } 
}

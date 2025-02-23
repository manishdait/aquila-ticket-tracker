package com.example.latte_api.comment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.example.latte_api.activity.Activity;
import com.example.latte_api.activity.ActivityService;
import com.example.latte_api.activity.dto.ActivityDto;
import com.example.latte_api.comment.dto.CommentRequest;
import com.example.latte_api.ticket.Ticket;
import com.example.latte_api.ticket.TicketRepository;
import com.example.latte_api.user.User;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
  private CommentService commentService;

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private ActivityService activityService;

  @Captor
  private ArgumentCaptor<Activity> activityCaptor;

  @BeforeEach
  void setup() {
    commentService = new CommentService(ticketRepository, activityService);
  }

  @AfterEach
  void purge() {
    commentService = null;
  }

  @Test
  void shouldReturn_activityDto_onCreateComment() {
    // mock
    final Ticket ticket = Mockito.mock(Ticket.class);
    final User user = Mockito.mock(User.class);
    final ActivityDto response = Mockito.mock(ActivityDto.class);

    // given
    final Authentication authentication = Mockito.mock(Authentication.class);
    final CommentRequest request = new CommentRequest("Message", 101L);

    // when
    when(authentication.getPrincipal()).thenReturn(user);
    when(ticketRepository.findById(request.ticketId())).thenReturn(Optional.of(ticket));
    when(activityService.createActivity(any(Activity.class))).thenReturn(response);
    final ActivityDto result = commentService.createComment(request, authentication);

    // then
    verify(authentication, times(1)).getPrincipal();
    verify(ticketRepository, times(1)).findById(request.ticketId());
    verify(activityService, times(1)).createActivity(any(Activity.class));

    Assertions.assertThat(result).isNotNull();
  }

  @Test
  void shouldThrow_exception_onCreateComment_forInvalidTicketId() {
    // mock
    final User user = Mockito.mock(User.class);

    // given
    final Authentication authentication = Mockito.mock(Authentication.class);
    final CommentRequest request = new CommentRequest("Message", 101L);

    // when
    when(authentication.getPrincipal()).thenReturn(user);
    when(ticketRepository.findById(request.ticketId())).thenReturn(Optional.empty());

    // then
    Assertions.assertThatThrownBy(() -> commentService.createComment(request, authentication));
  }
}

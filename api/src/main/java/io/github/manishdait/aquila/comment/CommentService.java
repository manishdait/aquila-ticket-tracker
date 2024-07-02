package io.github.manishdait.aquila.comment;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.manishdait.aquila.auth.AuthService;
import io.github.manishdait.aquila.ticket.Ticket;
import io.github.manishdait.aquila.ticket.TicketRepository;
import io.github.manishdait.aquila.users.User;
import io.github.manishdait.aquila.users.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    private final AuthService authService;

    public CommentResponse createComment (CommentRequest request) {
        User user = authService.getCurrentUser();
        Ticket ticket = ticketRepository.findById(request.ticketId()).orElseThrow();

        Comment comment = Comment.builder()
            .context(request.context())
            .createdBy(user)
            .ticket(ticket)
            .createdAt(Instant.now())
            .build();

        Comment response = commentRepository.save(comment);
        return mapToCommentResponse(response);
    }

    public List<CommentResponse> getComments () {
        return commentRepository.findAll()
            .stream()
            .map(c -> mapToCommentResponse(c))
            .toList();
    }

    public CommentResponse getComment (Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();

        return mapToCommentResponse(comment);
    }

    public List<CommentResponse> getCommentsByTicket (Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        List<Comment> comments = commentRepository.findByTicket(ticket).orElseThrow();

        return comments
            .stream()
            .map(c -> mapToCommentResponse(c))
            .toList();
    }

    public List<CommentResponse> getCommentsByUser (String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        List<Comment> comments = commentRepository.findByCreatedBy(user).orElseThrow();

        return comments
            .stream()
            .map(c -> mapToCommentResponse(c))
            .toList();
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return new CommentResponse(
            comment.getId(), 
            comment.getContext(), 
            comment.getCreatedBy().getUsername(), 
            comment.getTicket().getId(), 
            comment.getCreatedAt()
        );
    }
}

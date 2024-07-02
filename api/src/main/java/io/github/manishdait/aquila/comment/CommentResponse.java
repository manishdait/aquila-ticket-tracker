package io.github.manishdait.aquila.comment;

import java.time.Instant;

public record CommentResponse (Long id, String context, String createdBy, Long ticketId, Instant createdAt) {
}

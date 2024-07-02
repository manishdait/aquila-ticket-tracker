package io.github.manishdait.aquila.ticket;

import java.util.List;

public record TicketRequest (String title, String description, Priority priority, List<String> assignees, Long projectId) {
    
}

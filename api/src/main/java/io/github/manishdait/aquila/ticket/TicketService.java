package io.github.manishdait.aquila.ticket;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.manishdait.aquila.auth.AuthService;
import io.github.manishdait.aquila.comment.CommentRepository;
import io.github.manishdait.aquila.project.Project;
import io.github.manishdait.aquila.project.ProjectRepository;
import io.github.manishdait.aquila.users.User;
import io.github.manishdait.aquila.users.UserRepository;
import io.github.manishdait.aquila.users.UserResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;

    private final AuthService authService;

    public TicketResponse createTicket (TicketRequest request) {
        User user = authService.getCurrentUser();
        Project project = projectRepository.findById(request.projectId()).orElseThrow();

        Ticket ticket = Ticket.builder()
            .title(request.title())
            .description(request.description())
            .priority(request.priority())
            .status(Status.OPEN)
            .createAt(Instant.now())
            .updatedAt(Instant.now())
            .reportedBy(user)
            .assignees(
                request.assignees()
                    .stream()
                    .map(u -> mapToUser(u))
                    .collect(Collectors.toList())
            )
            .project(project)
            .build();

        Ticket response = ticketRepository.save(ticket);
        
        return mapToTicketResponse(response);
    }

    public List<TicketResponse> getTickets () {
        return ticketRepository.findAll()
            .stream()
            .map(t -> mapToTicketResponse(t))
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketByUser (String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByReportedBy(user).orElseThrow();

        return tickets
            .stream()
            .map(t -> mapToTicketResponse(t))
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketByAssignee (String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByAssigneesContainingIgnoreCase(user).orElseThrow();

        return tickets
            .stream()
            .map(t -> mapToTicketResponse(t))
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketByProject (Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByProject(project).orElseThrow();

        return tickets
            .stream()
            .map(t -> mapToTicketResponse(t))
            .collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketByProject (String code) {
        Project project = projectRepository.findByCode(code).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByProject(project).orElseThrow();

        return tickets
            .stream()
            .map(t -> mapToTicketResponse(t))
            .collect(Collectors.toList());
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        return mapToTicketResponse(ticket);
    }

    public TicketResponse updateTicket (TicketResponse request) {
        Ticket ticket = ticketRepository.findById(request.id()).orElseThrow(); 
        
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(request.status());
        ticket.setUpdatedAt(Instant.now());
        ticket.setAssignees(
                request.assignees()
                    .stream()
                    .map(u -> mapToUser(u.name()))
                    .collect(Collectors.toList())
        );

        Ticket response = ticketRepository.save(ticket);
        
        return mapToTicketResponse(response);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketResponse mapToTicketResponse (Ticket ticket) {
        return new TicketResponse(
            ticket.getId(), 
            ticket.getTitle(), 
            ticket.getDescription(), 
            ticket.getCreateAt(), 
            ticket.getUpdatedAt(), 
            ticket.getPriority(), 
            ticket.getStatus(), 
            new UserResponse(
                ticket.getReportedBy().getUsername(), 
                ticket.getReportedBy().getEmail(), 
                ticket.getReportedBy().getRole().name(), 
                ticket.getReportedBy().isEnabled()
            ), 
            ticket.getAssignees().stream().map(
                u -> new UserResponse(u.getUsername(), u.getEmail(), u.getRole().name(), u.isEnabled())
            ).collect(Collectors.toList()), 
            ticket.getProject().getId(), 
            ticket.getProject().getCode(), 
            commentRepository.findByTicket(ticket).get().size()
        );
    }

    private User mapToUser (String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}

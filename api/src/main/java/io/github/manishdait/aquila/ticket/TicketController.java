package io.github.manishdait.aquila.ticket;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.ExceptionResponse;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/aquila-api/ticket")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(request));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets() {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicket(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<TicketResponse>> getTicketByUser(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByUser(username));
    }

    @GetMapping("/by-assignee/{username}")
    public ResponseEntity<List<TicketResponse>> getTicketByAssignee(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByAssignee(username));
    }

    @GetMapping("/by-project/{id}")
    public ResponseEntity<List<TicketResponse>> getTicketByProject(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByProject(id));
    }

    @GetMapping("/by-project-code/{code}")
    public ResponseEntity<List<TicketResponse>> getTicketByProject(@PathVariable String code) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByProject(code));
    }

    @PutMapping
    public ResponseEntity<TicketResponse> updateTicket(@RequestBody TicketResponse request) {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.updateTicket(request));
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }

    @ExceptionHandler(AquilaApiException.class)
    public ResponseEntity<ExceptionResponse> handleException(AquilaApiException e) {
        return ResponseEntity.status(e.getStatus())
        .body(new ExceptionResponse(e.getStatus().value(), e.getTimestamp(), e.getError(), e.getMessage()));
    }
}

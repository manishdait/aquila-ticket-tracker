package com.example.latte_api.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.latte_api.user.User;


public interface TicketRepository extends JpaRepository<Ticket, Long> {
  Page<Ticket> findByCreatedBy(Pageable pageable, User createdBy);
}

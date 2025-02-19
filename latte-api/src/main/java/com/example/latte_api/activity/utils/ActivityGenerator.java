package com.example.latte_api.activity.utils;

import org.springframework.stereotype.Component;

import com.example.latte_api.activity.Activity;
import com.example.latte_api.activity.enums.ActivityType;
import com.example.latte_api.ticket.Ticket;
import com.example.latte_api.ticket.enums.Priority;
import com.example.latte_api.ticket.enums.Status;
import com.example.latte_api.user.User;

@Component
public class ActivityGenerator {
  public Activity ticketCreated(User user, Ticket ticket) {
    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(String.format("%s created ticked", user.getFirstname()))
      .build();
  }

  public Activity titleChanged(User user, Ticket ticket, String old, String curr) {
    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(String.format("%s change title from %s to %s", user.getFirstname(), old, curr))
      .build();
  }

  public Activity descriptionChanged(User user, Ticket ticket) {
    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(String.format("%s edited the description of ticket", user.getFirstname()))
      .build();
  }

  public Activity assignedToChanged(User user, Ticket ticket, String old, String curr) {
    String msg = null;

    if (old.isEmpty() && !curr.isEmpty()) {
      msg = String.format("%s assigned ticket to %s", user.getFirstname(), curr);
    }
    if (!old.isEmpty() && !curr.isEmpty()) {
      msg = String.format("%s unassigned %s and assigned ticket to %s", user.getFirstname(), old, curr);
    }
    if (!old.isEmpty() && curr.isEmpty()) {
      msg = String.format("%s unassigned %s", user.getFirstname(), old);
    }

    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(msg)
      .build();
  }

  public Activity priorityChanged(User user, Ticket ticket, Priority old, Priority curr) {
    String msg = String.format("%s change priority from %s to %s", user.getFirstname(), old.toString(), curr.toString());
    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(msg)
      .build();
  }

  public Activity statusChanged(User user, Ticket ticket, Status old, Status curr) {
    String msg = String.format("%s change status from %s to %s", user.getFirstname(), old.toString(), curr.toString());
    return Activity.builder()
      .type(ActivityType.EDIT)
      .author(user)
      .ticket(ticket)
      .message(msg)
      .build();
  }
}

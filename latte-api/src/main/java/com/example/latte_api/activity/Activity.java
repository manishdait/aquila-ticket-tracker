package com.example.latte_api.activity;

import com.example.latte_api.activity.enums.ActivityType;
import com.example.latte_api.shared.AbstractAuditingEntity;
import com.example.latte_api.ticket.Ticket;
import com.example.latte_api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "activity")
public class Activity extends AbstractAuditingEntity  {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq_generator")
  @SequenceGenerator(name = "activity_seq_generator", sequenceName = "activity_seq", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "type")
  private ActivityType type;

  @Column(name = "message")
  private String message;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "ticket_id")
  private Ticket ticket;
}

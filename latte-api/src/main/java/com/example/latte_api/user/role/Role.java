package com.example.latte_api.user.role;

import java.util.List;

import com.example.latte_api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq_generator")
  @SequenceGenerator(name = "role_seq_generator", sequenceName = "role_seq", initialValue = 101, allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "role", unique = true)
  private String role;

  @JsonIgnore
  @OneToMany(mappedBy = "role")
  private List<User> users;
}

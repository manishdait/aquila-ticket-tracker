package io.github.manishdait.aquila.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.manishdait.aquila.users.User;

@Repository
public interface ProjectRepository extends JpaRepository <Project, Long> {
    Optional<List<Project>> findByTeamMembersContainingIgnoreCase(User user);
    Optional<Project> findByCode(String code);
    Optional<Project> findByName(String name);
}

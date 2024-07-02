package io.github.manishdait.aquila.project;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.manishdait.aquila.auth.AuthService;
import io.github.manishdait.aquila.error.AquilaApiException;
import io.github.manishdait.aquila.error.Error;
import io.github.manishdait.aquila.ticket.TicketRepository;
import io.github.manishdait.aquila.users.User;
import io.github.manishdait.aquila.users.UserRepository;
import io.github.manishdait.aquila.users.UserResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    private final AuthService authService;

    public ProjectResponse createProject(ProjectRequest request) {
        User user = authService.getCurrentUser();
        request.teamMembers().add(user.getUsername());

        Optional<Project> duplicate = projectRepository.findByName(request.name());  
        if (duplicate.isPresent()) {
            throw new AquilaApiException(
                HttpStatus.NOT_ACCEPTABLE, 
                Error.DUPLICATE_VALUE_ERROR.error(), 
                String.format("The project name '%s' is already in use.Use different name.", request.name()), 
                Instant.now()
            );
        }    
        
        duplicate = projectRepository.findByCode(request.code());  
        if (duplicate.isPresent()) {
            throw new AquilaApiException(
                HttpStatus.NOT_ACCEPTABLE, 
                Error.DUPLICATE_VALUE_ERROR.error(), 
                String.format("The project code '%s' is already in use.Use different code.", request.name()), 
                Instant.now()
            );
        }

        Project project = Project.builder().name(request.name())
            .description(request.description())
            .createdAt(Instant.now())
            .teamMembers(
                request.teamMembers()
                    .stream()
                    .map(u -> mapToUser(u))
                    .collect(Collectors.toList())
            )
            .code(request.code())
            .build();

        Project response = projectRepository.save(project);
        return mapToProjectResponse(response);
    }

    public List<ProjectResponse> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects
            .stream()
            .map(p -> mapToProjectResponse(p))
            .collect(Collectors.toList());
    }

    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_FOUND, 
                Error.PROJECT_NOT_FOUND.error(), 
                String.format("The project id '%d' was not found.Please check if project is valid.", id), 
                Instant.now()
            )
        );
        return mapToProjectResponse(project);
    }

    public ProjectResponse getProjectByCode(String code) {
        Project project = projectRepository.findByCode(code).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_FOUND, 
                Error.PROJECT_NOT_FOUND.error(), 
                String.format("The project code '%d' was not found.Please check if project is valid.", code), 
                Instant.now()
            )
        );
        return mapToProjectResponse(project);
    }

    public List<ProjectResponse> getProjectByUsername(String username) {
        User user = mapToUser(username);
        List<Project> projects = projectRepository
            .findByTeamMembersContainingIgnoreCase(user)
            .orElseThrow(
                () -> new AquilaApiException(
                HttpStatus.NOT_ACCEPTABLE, 
                Error.DUPLICATE_VALUE_ERROR.error(), 
                String.format("The username '%s' is already in use.Use different username.", username), 
                Instant.now()
            )
            );
            
        return projects
            .stream()
            .map(p -> mapToProjectResponse(p))
            .collect(Collectors.toList());
    }

    public ProjectResponse updateProject(ProjectResponse request) {
        Project project = projectRepository.findById(request.id()).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_FOUND, 
                Error.PROJECT_NOT_FOUND.error(), 
                String.format("The project id '%d' was not found.Please check if project is valid.", request.id()), 
                Instant.now()
            )
        );
        project.setDescription(request.description());
        project.setName(request.name());
        project.setCode(request.code());
        project.setTeamMembers(
            request.teamMembers()
                .stream()
                .map(u -> mapToUser(u.name()))
                .collect(Collectors.toList())
        );
        projectRepository.save(project);

        return mapToProjectResponse(project);
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        return new ProjectResponse(
            project.getId(), 
            project.getCode(), 
            project.getName(), 
            project.getDescription(), 
            ticketRepository.findByProject(project).get().size(), 
            project.getCreatedAt(),  
            project.getTeamMembers().stream().map(
                u -> new UserResponse(u.getUsername(), u.getEmail(), u.getRole().name(), u.isEnabled())
            ).collect(Collectors.toList())
        );
    }

    private User mapToUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
            () -> new AquilaApiException(
                HttpStatus.NOT_ACCEPTABLE, 
                Error.DUPLICATE_VALUE_ERROR.error(), 
                String.format("The username '%s' is already in use.Use different username.", username), 
                Instant.now()
            )
        );
    }
}

package io.github.manishdait.aquila.project;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/aquila-api/project")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProject(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<ProjectResponse> getProjectByCode(@PathVariable String code) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectByCode(code));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<ProjectResponse>> getProjectByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body( projectService.getProjectByUsername(username));
    }

    @PutMapping
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody ProjectResponse request) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.updateProject(request));
    }
}

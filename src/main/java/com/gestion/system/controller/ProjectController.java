package com.gestion.system.controller;

import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ProjectController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                                private static final Integer TEMP_USER_ID = 1;
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Integer id) {
        ProjectResponse response = projectService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> getByName(@RequestParam String name) {
        List<ProjectResponse> response = projectService.searchByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProjectResponse>> getAll() {
        List<ProjectResponse> response = projectService.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse response = projectService.create(projectRequest, TEMP_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> update(@PathVariable Integer id,
                                                  @Valid @RequestBody ProjectUpdateRequest updateRequest) {
        ProjectResponse response = projectService.update(id, updateRequest, TEMP_USER_ID);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        projectService.delete(id, TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }

}

package com.gestion.system.controller;

import com.gestion.system.dto.request.create.UserProjectRequest;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.service.UserProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-projects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserProjectController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                    private static final Integer TEMP_USER_ID = 1;
    private final UserProjectService userProjectService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProjectResponse> getById(@PathVariable Integer id) {
        UserProjectResponse response = userProjectService.getById(TEMP_USER_ID, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<List<UserProjectResponse>> getAllByUser(@PathVariable Integer idUser) {
        List<UserProjectResponse> responses = userProjectService.getAllProjectsByUser(TEMP_USER_ID, idUser);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/projects/{idProject}")
    public ResponseEntity<List<UserProjectResponse>> getAllByProject(@PathVariable Integer idProject) {
        List<UserProjectResponse> responses = userProjectService.getAllUsersByProjects(TEMP_USER_ID, idProject);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<UserProjectResponse> create(@Valid @RequestBody UserProjectRequest userProjectRequest) {
        UserProjectResponse response = userProjectService.create(TEMP_USER_ID, userProjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userProjectService.delete(TEMP_USER_ID, id);
        return ResponseEntity.noContent().build();
    }

}

package com.gestion.system.controller;

import com.gestion.system.dto.request.create.TaskRequest;
import com.gestion.system.dto.request.filters.TaskDatesFilterRequest;
import com.gestion.system.dto.request.filters.TaskFilterRequest;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class TaskController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                    private static final Integer TEMP_USER_ID = 1;
    private final TaskService taskService;

    // Aqui se contiene las consultas con el filter para evitar abrir mucho endpoints
    //    Solo funciona con 1 de las tres o con las 3 a la vez, no con combinaciones de valores
    @GetMapping("/projects/{idProject}/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TaskResponse>> getAllByProjects(@PathVariable Integer idProject,
                                                               @ModelAttribute TaskFilterRequest filters) {
        List<TaskResponse> responses = taskService.getByProjectId(idProject, filters);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/{idTask}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TaskResponse> getById(@PathVariable Integer idTask) {
        TaskResponse taskResponse = taskService.getById(idTask);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/projects/{idProject}/tasks/active")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TaskResponse>> getAllActiveByProjects(@PathVariable Integer idProject) {
        List<TaskResponse> response = taskService.activeList(idProject);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{idProject}/tasks/dates")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TaskResponse>> getAllDatesByProjects(@PathVariable Integer idProject,
                                                                    @Valid @ModelAttribute TaskDatesFilterRequest filter) {
       List<TaskResponse> responses = taskService.betweenDates(idProject, filter.getStartDate(), filter.getEndDate());
       return ResponseEntity.ok(responses);
    }

    @PostMapping("projects/{idProject}/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TaskResponse> create(@PathVariable Integer idProject,
                                               @Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.create(TEMP_USER_ID,taskRequest, idProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @PatchMapping("/projects/{idProject}/tasks/{idTask}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<TaskResponse> update(@PathVariable Integer idTask, @PathVariable Integer idProject,
                                               @Valid @RequestBody TaskUpdateRequest updateRequest) {
        TaskResponse response = taskService.update(TEMP_USER_ID,updateRequest, idProject, idTask);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/projects/{idProject}/tasks/{idTask}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer idTask, @PathVariable Integer idProject) {
        taskService.delete(TEMP_USER_ID, idProject, idTask);
        return ResponseEntity.noContent().build();
    }

}

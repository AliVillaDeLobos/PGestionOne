package com.gestion.system.controller;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;
import com.gestion.system.service.TaskAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-assignments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class TaskAssignmentController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                        private static final Integer TEMP_USER_ID = 1;
    private final TaskAssignmentService taskAssignmentService;

    @GetMapping("/tasks/{idTask}")
    public ResponseEntity<List<TaskAssignmentResponse>> getTaskAssignment(@PathVariable Integer idTask) {
        List<TaskAssignmentResponse> response = taskAssignmentService.getAllByTask(TEMP_USER_ID, idTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/projects/{idProject}")
    public ResponseEntity<List<TaskAssignmentResponse>> getTaskAssignmentByProject(@PathVariable Integer idProject) {
        List<TaskAssignmentResponse> response = taskAssignmentService.getAllByProject(TEMP_USER_ID, idProject);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{idUserRequest}")
    public ResponseEntity<List<TaskAssignmentResponse>> getTaskAssignmentByUser(@PathVariable Integer idUserRequest) {
        List<TaskAssignmentResponse> response = taskAssignmentService.getAllByUser(TEMP_USER_ID, idUserRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskAssignmentResponse> saveTaskAssignment(@Valid @RequestBody TaskAssignmentRequest taskAssignmentRequest) {
        TaskAssignmentResponse response = taskAssignmentService.assign(TEMP_USER_ID, taskAssignmentRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{idTaskAssignment}")
    public ResponseEntity<Void> deleteTaskAssignment(@PathVariable Integer idTaskAssignment) {
        taskAssignmentService.unassign(TEMP_USER_ID, idTaskAssignment);
        return ResponseEntity.noContent().build();
    }


}

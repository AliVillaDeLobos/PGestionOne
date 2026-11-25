package com.gestion.system.controller;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.model.enums.Action;
import com.gestion.system.service.TaskAssignmentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-assignment-history")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TaskAssignmentHistoryController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                        private static final Integer TEMP_USER_ID = 1;
    private final TaskAssignmentHistoryService taskAssignmentHistoryService;

    @GetMapping("/tasks/{idTask}")
    public ResponseEntity<List<TaskAssignmentHistoryResponse>> getAllByTaskAndAction(@PathVariable Integer idTask,
                                                                              @RequestParam Action action) {
        List<TaskAssignmentHistoryResponse> response = taskAssignmentHistoryService
                .getAllByTaskAndAction(TEMP_USER_ID, idTask, action);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{idProject}")
    public ResponseEntity<List<TaskAssignmentHistoryResponse>> getAllByProjectAndAction(@PathVariable Integer idProject,
                                                                                        @RequestParam Action action) {
        List<TaskAssignmentHistoryResponse> response = taskAssignmentHistoryService.
                getAllByProjectAndAction(TEMP_USER_ID, idProject, action);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{idUserRequest}")
    public ResponseEntity<List<TaskAssignmentHistoryResponse>> getAllByUserAndAction(@PathVariable Integer idUserRequest,
                                                                                     @RequestParam Action action) {
        List<TaskAssignmentHistoryResponse> response = taskAssignmentHistoryService
                .getAllByUserAndAction(TEMP_USER_ID, idUserRequest,action);
        return ResponseEntity.ok(response);
    }

}

package com.gestion.system.controller;

import com.gestion.system.dto.request.create.SubtaskRequest;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.service.SubtaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class SubtaskController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                            private static final Integer TEMP_USER_ID = 1;
    private final SubtaskService subtaskService;

    @GetMapping("subtasks/{idSubtask}")
    public ResponseEntity<SubtaskResponse> getSubtask(@PathVariable Integer idSubtask) {
        SubtaskResponse response = subtaskService.getById(idSubtask);
        return  ResponseEntity.ok(response);
    }

    @PatchMapping("/subtasks/{idSubtask}/restore")
    public ResponseEntity<SubtaskResponse> restore(@PathVariable Integer idSubtask) {
        SubtaskResponse response = subtaskService.restore(TEMP_USER_ID,  idSubtask);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/{idTask}/subtasks")
    public ResponseEntity<List<SubtaskResponse>> getAllByTask(@PathVariable Integer idTask) {
        List<SubtaskResponse> responses = subtaskService.getAllByTaskId(idTask);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/{idTask}/subtasks/deleted")
    public ResponseEntity<List<SubtaskResponse>> getAllDeletes(@PathVariable Integer idTask) {
        List<SubtaskResponse> responses = subtaskService.getAllIsDeleted(idTask);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/{idTask}/subtask/pending")
    public ResponseEntity<List<SubtaskResponse>> getAllNoComplete(@PathVariable Integer idTask) {
        List<SubtaskResponse> responses = subtaskService.getAllNotCompleted(idTask);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tasks/{idTask}/subtasks/completed")
    public ResponseEntity<List<SubtaskResponse>> getAllCompletes(@PathVariable Integer idTask) {
        List<SubtaskResponse> responses = subtaskService.getAllCompleted(idTask);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/tasks/{idTask}/subtasks")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<SubtaskResponse> create(@PathVariable Integer idTask,
                                                  @Valid @RequestBody SubtaskRequest subtaskRequest) {
        SubtaskResponse response = subtaskService.create(TEMP_USER_ID, subtaskRequest, idTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/task/{idTask}/subtasks/{idSubtask}")
    public ResponseEntity<SubtaskResponse> update(@PathVariable Integer idTask, @PathVariable Integer idSubtask,
                                                  @Valid @RequestBody SubtaskUpdateRequest updateRequest) {
        SubtaskResponse response = subtaskService.update(TEMP_USER_ID, idSubtask, updateRequest, idTask);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/task/{idTask}/subtasks/{idSubtask}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer idTask, @PathVariable Integer idSubtask,
                                                  @NotBlank @RequestBody String massage) {
        subtaskService.delete(TEMP_USER_ID, idSubtask, massage, idTask);
        return ResponseEntity.noContent().build();
    }

}

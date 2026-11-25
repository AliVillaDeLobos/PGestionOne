package com.gestion.system.controller;

import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.service.DaySubtaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day-subtasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEMBER')")
public class DaySubtaskController {
    //                            Id = 1 del User es temporal hasta agregar SecurityContext
                                    private static final Integer TEMP_USER_ID = 1;

    private final DaySubtaskService daySubtaskService;

    @GetMapping("/{id}")
    public ResponseEntity<DaySubtaskResponse> getDaySubtask(@PathVariable Integer id) {
        DaySubtaskResponse response = daySubtaskService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subtask/{idSubtask}")
    public ResponseEntity<List<DaySubtaskResponse>> getAllBySubtask(@PathVariable Integer idSubtask) {
        List<DaySubtaskResponse> responses = daySubtaskService.getByIdSubtask(idSubtask);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/day/{idDay}")
    public ResponseEntity<List<DaySubtaskResponse>> getAllByDay(@PathVariable Integer idDay) {
        List<DaySubtaskResponse> responses = daySubtaskService.getByIdDay(idDay);
        return ResponseEntity.ok(responses);
    }

    @PostMapping()
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DaySubtaskResponse> create(@Valid @RequestBody DaySubtaskRequest daySubtaskRequest) {
        DaySubtaskResponse response = daySubtaskService.create(daySubtaskRequest, TEMP_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DaySubtaskResponse> update(@PathVariable Integer id,
                                                     @Valid @RequestBody DaySubtaskUpdateRequest updateRequest) {
        DaySubtaskResponse response = daySubtaskService.update(id, updateRequest, TEMP_USER_ID);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        daySubtaskService.delete(id, TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }

}

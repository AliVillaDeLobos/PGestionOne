package com.gestion.system.controller;

import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.request.update.DaysHoursUpdateRequest;
import com.gestion.system.dto.response.DaysHoursResponse;
import com.gestion.system.service.DaysHoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/days-hours")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANGER','MEMBER','ADMIN','ROOT')")
public class DaysHoursController {
                            //Id = 1 del User es temporal hasta agregar SecurityContext
                            private static final Integer TEMP_USER_ID = 1;
    private final DaysHoursService daysHoursService;


    @GetMapping("/{id}")
    public ResponseEntity<DaysHoursResponse> getById(@PathVariable Integer id) {
        DaysHoursResponse response = daysHoursService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subtask/{idSubtask}")
    public ResponseEntity<List<DaysHoursResponse>> getAllBySubtask(@PathVariable Integer idSubtask) {
        List<DaysHoursResponse> responses = daysHoursService.getBySubtaskId(idSubtask);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/subtask/{idSubtask}")
    public ResponseEntity<DaysHoursResponse> create(@PathVariable Integer idSubtask, @Valid @RequestBody DaysHoursRequest requestDto) {
        DaysHoursResponse response = daysHoursService.create(requestDto, idSubtask, TEMP_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DaysHoursResponse> update(@PathVariable Integer id, @Valid @RequestBody DaysHoursUpdateRequest requestUpdate) {
        DaysHoursResponse response = daysHoursService.update(id, requestUpdate, TEMP_USER_ID);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DaysHoursResponse> delete(@PathVariable Integer id) {
        daysHoursService.delete(id, TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }

}

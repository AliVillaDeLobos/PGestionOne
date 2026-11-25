package com.gestion.system.controller;

import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Operation;
import com.gestion.system.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {
                                //Id = 1 del User es temporal hasta agregar SecurityContext
                                private static final Integer TEMP_USER_ID = 1;

    private final AuditService auditService;

     @GetMapping("/user/{email}")
    public ResponseEntity<List<AuditResponse>> getAllByUser(@PathVariable String email) {
        List<AuditResponse> response = auditService.getAllByUser(TEMP_USER_ID, email);
        return ResponseEntity.ok(response);
     }

     @GetMapping("/table/{table}")
    public ResponseEntity<List<AuditResponse>> getAllByTable(@PathVariable  AuditableEntity table) {
        List<AuditResponse> response = auditService.getAllByTable(TEMP_USER_ID, table);
        return ResponseEntity.ok(response);
     }

     @GetMapping("/record/{recordId}")
    public ResponseEntity<List<AuditResponse>> getAllByRecordId(@PathVariable Integer recordId) {
         List<AuditResponse> responses = auditService.getAllByRecord(TEMP_USER_ID, recordId);
         return ResponseEntity.ok(responses);
     }

     @GetMapping("/{operation}/by-dates")
    public ResponseEntity<List<AuditResponse>> getAllByDates(@PathVariable Operation operation, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
         List<AuditResponse> responses = auditService.getAllOperationByDate(TEMP_USER_ID, operation, startDate, endDate);
        return ResponseEntity.ok(responses);
     }
}

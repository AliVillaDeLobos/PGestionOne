package com.gestion.system.controller;

import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Operation;
import com.gestion.system.service.AuditService;
import com.gestion.system.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
public class AuditController {
                                //Id = 1 del User es temporal hasta agregar SecurityContext
    private final AuditService auditService;

     @GetMapping("/user/{email}")
    public ResponseEntity<List<AuditResponse>> getAllByUser(@PathVariable String email) {
        List<AuditResponse> response = auditService.getAllByUser( 1, email);
        return ResponseEntity.ok(response);
     }

     @GetMapping("/table/{table}")
    public ResponseEntity<List<AuditResponse>> getAllByTable(@PathVariable  AuditableEntity table) {
        List<AuditResponse> response = auditService.getAllByTable( 1, table);
        return ResponseEntity.ok(response);
     }

     @GetMapping("/record/{recordId}")
    public ResponseEntity<List<AuditResponse>> getAllByRecordId(@PathVariable Integer recordId) {
         List<AuditResponse> responses = auditService.getAllByRecord(1, recordId);
         return ResponseEntity.ok(responses);
     }

     @GetMapping("/{operation}/by-dates")
    public ResponseEntity<List<AuditResponse>> getAllByDates(@PathVariable Operation operation, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
         List<AuditResponse> responses = auditService.getAllOperationByDate(1, operation, startDate, endDate);
        return ResponseEntity.ok(responses);
     }
}

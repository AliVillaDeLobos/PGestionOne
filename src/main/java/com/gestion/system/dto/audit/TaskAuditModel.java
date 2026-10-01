package com.gestion.system.dto.audit;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAuditModel {
    private String name;
    private Integer idProject;
    private String nameProject;
    private String color;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}

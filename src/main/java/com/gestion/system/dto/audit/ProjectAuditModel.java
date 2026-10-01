package com.gestion.system.dto.audit;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectAuditModel {
    private String name;
    private LocalDate startDate;
}

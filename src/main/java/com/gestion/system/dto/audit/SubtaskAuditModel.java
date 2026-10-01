package com.gestion.system.dto.audit;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtaskAuditModel {

    private Integer idTask;
    private String nameTask;
    private String nameSubtask;
    private Boolean completed;
    private Boolean isDeleted;
    private LocalDate startDate;


}

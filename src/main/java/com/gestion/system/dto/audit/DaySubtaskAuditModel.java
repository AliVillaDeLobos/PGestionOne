package com.gestion.system.dto.audit;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DaySubtaskAuditModel {
    private Integer idSubtask;
    private String nameSubtask;
    private Integer idDay;
    private String status;

}

package com.gestion.system.dto.audit;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayHoursAuditModel {
    private Integer idDaySubtask;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}

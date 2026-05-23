package com.gestion.system.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DaysHoursResponse {

    private Integer id;
    private DaySubtaskResponse daySubtask;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


}

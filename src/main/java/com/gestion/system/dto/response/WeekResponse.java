package com.gestion.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeekResponse {

    private Integer weekNumber;
    private Integer year;
    private LocalDate startDate;
    private LocalDate endDate;

}

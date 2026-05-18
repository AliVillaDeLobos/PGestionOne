package com.gestion.system.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayResponse {

    private LocalDate date;
    private String dayName;
    private WeekResponse week;

}

package com.gestion.system.dto.request;

import com.gestion.system.validations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidDateRange(start = "startTime", end = "endTime")
public class DaysHoursRequest {

     @Positive
    private Integer idDaySubtask;

     @NotNull
     @FutureOrPresent
    private LocalDateTime startTime;

     @NotNull
     @FutureOrPresent
    private LocalDateTime endTime;

}

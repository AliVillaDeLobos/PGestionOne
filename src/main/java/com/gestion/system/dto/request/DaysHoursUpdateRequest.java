package com.gestion.system.dto.request;

import com.gestion.system.validations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidDateRange(start = "startTime", end = "endTime")
public class DaysHoursUpdateRequest {

     @FutureOrPresent
    private LocalDateTime startTime;
     @FutureOrPresent
    private LocalDateTime endTime;

}

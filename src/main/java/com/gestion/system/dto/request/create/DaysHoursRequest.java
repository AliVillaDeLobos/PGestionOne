package com.gestion.system.dto.request.create;

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
public class DaysHoursRequest {


     @NotNull(message = "La fecha inicial de la tarea es obligarotia.")
     @FutureOrPresent
    private LocalDateTime startTime;

     @NotNull(message = "La fecha del final de la tarea es obligarotia.")
     @FutureOrPresent
    private LocalDateTime endTime;

}

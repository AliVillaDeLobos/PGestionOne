package com.gestion.system.dto.request.update;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import com.gestion.system.validations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidDateRange(start = "startDate", end = "endDate")
public class TaskUpdateRequest {

    private Colors color;
    private String name;
    private Status status;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;


}

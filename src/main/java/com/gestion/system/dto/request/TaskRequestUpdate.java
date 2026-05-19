package com.gestion.system.dto.request;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import com.gestion.system.validations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidDateRange(start = "startDate", end = "endDate")
public class TaskRequestUpdate {

    private Colors color;
    private String name;
    private Status status;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;


}

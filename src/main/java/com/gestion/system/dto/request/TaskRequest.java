package com.gestion.system.dto.request;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import com.gestion.system.validations.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidDateRange(start = "startDate", end = "endDate")
public class TaskRequest {

     @NotNull(message = "El color es obligatorio. " +
            "\nRevisa la lista de colores en el enum 'colors-enum'.")
    private Colors color;

     @NotBlank(message = "Es obligatorio el nombre de la Tarea.")
    private String name;

    private Status status;

     @NotNull(message = "La fecha inicial de la tarea es obligatoria.")
     @FutureOrPresent
    private LocalDate startDate;

     @NotNull(message = "La fecha del final de la tarea es obligatoria.")
     @FutureOrPresent
    private LocalDate endDate;

}

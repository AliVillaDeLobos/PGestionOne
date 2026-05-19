package com.gestion.system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySubtaskRequest {

     @Positive(message = "EL Id no puede ser negativo.")
     @NotNull(message = "El obligatorio asignarle un Id de Subtask.")
    private Integer idSubtask;


     @Positive(message = "EL Id no puede ser negativo.")
     @NotNull(message = "El obligatorio asignarle un Id de Day.")
    private Integer idDay;
}

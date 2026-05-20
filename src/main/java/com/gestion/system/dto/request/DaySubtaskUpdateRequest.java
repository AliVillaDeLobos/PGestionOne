package com.gestion.system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySubtaskUpdateRequest {
     @Positive(message = "EL Id no puede ser negativo.")
    private Integer idSubtask;

     @Positive(message = "EL Id no puede ser negativo.")
    private Integer idDay;
}

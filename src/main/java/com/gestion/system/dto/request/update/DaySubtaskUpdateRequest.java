package com.gestion.system.dto.request.update;

import com.gestion.system.model.enums.Status;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySubtaskUpdateRequest {

     @Positive(message = "ID cannot be negative number.")
    private Integer idDay;
    private Status status;
}

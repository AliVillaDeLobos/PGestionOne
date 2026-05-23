package com.gestion.system.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectUpdateRequest {

     @Size(min = 2, max = 50, message = "El nombre tiene que tener minimo 2 caracteres y un maximo de 50.")
    private String name;

     @Size(min = 10, max = 500, message = "Requiere minimo 10 caracteres y un maximo de 500")
    private String description;
}

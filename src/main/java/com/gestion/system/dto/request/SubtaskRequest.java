package com.gestion.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskRequest {

     @Schema(description = "Campo pcional pero recomendado.")
    private String description;

     @NotBlank(message = "Es necesario asiganrl e un nombre a la Subtask nueva.")
    private String name;


}

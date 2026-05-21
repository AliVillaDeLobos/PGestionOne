package com.gestion.system.dto.request.create;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectRequest {

     @NotNull(message = "Usuer ID is required.")
     @Positive
    private Integer userId;

     @NotNull(message = "Usuer ID is required.")
     @Positive
    private Integer projectId;
}

package com.gestion.system.dto.request.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleRequest {

     @NotNull(message = "USER ID is required.")
     @Positive
    private Integer userId;

     @NotNull(message = "ROLE ID is required.")
     @Positive
    private Integer roleId;

}

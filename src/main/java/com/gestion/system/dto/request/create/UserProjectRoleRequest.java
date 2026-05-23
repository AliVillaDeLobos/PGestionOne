package com.gestion.system.dto.request.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectRoleRequest {

     @NotNull(message = "UserProject ID is required.")
     @Positive
    private Integer userProjectId;
     @NotNull(message = "Role is requeried.")
     @Positive
    private Integer roleId;

}

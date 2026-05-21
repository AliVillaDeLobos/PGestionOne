package com.gestion.system.dto.request.update;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectRoleUpdateRequest {

     @Positive
    private Integer role;

}

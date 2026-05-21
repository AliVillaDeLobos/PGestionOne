package com.gestion.system.dto.request.update;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleUpdateRequest {
     @Positive
    private Integer userId;
     @Positive
    private Integer roleId;
}

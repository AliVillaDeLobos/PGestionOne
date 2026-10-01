package com.gestion.system.dto.response;

import com.gestion.system.model.enums.SystemRole;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private String name;
    private String lastNames;
    private String email;
    private String role;
}

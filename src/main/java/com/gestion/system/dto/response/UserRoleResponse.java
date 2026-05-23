package com.gestion.system.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {

    private Integer id;
    private UserResponse user;
    private RolesResponse role;

}

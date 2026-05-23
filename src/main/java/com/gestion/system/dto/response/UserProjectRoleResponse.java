package com.gestion.system.dto.response;

import com.gestion.system.model.entities.Roles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectRoleResponse {

    private Integer id;
    private UserProjectResponse userProject;
    private RolesResponse role;

}

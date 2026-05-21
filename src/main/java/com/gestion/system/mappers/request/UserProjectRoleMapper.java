package com.gestion.system.mappers.request;

import com.gestion.system.dto.response.UserProjectRoleResponse;
import com.gestion.system.mappers.response.RolesMapper;
import com.gestion.system.model.entities.Roles;
import com.gestion.system.model.entities.UserProject;
import com.gestion.system.model.entities.UserProjectRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserProjectRoleMapper {
    private final UserProjectMapper userProjectMapper;
    private final RolesMapper  rolesMapper;

    public UserProjectRoleMapper(UserProjectMapper userProjectMapper, RolesMapper rolesMapper) {
        this.userProjectMapper = userProjectMapper;
        this.rolesMapper = rolesMapper;
    }

    public UserProjectRoleResponse toResponse(UserProjectRole entity){
        return entity == null ? null
                : UserProjectRoleResponse.builder()
                .id(entity.getId())
                .userProject(userProjectMapper.toResponse(entity.getUserProject()))
                .role(rolesMapper.toResponse(entity.getRole()))
                .build();
    }

    public UserProjectRole responseToEntity(UserProject userProject, Roles role){
        if (userProject == null || role == null)
            throw new IllegalArgumentException("UserProject or Role cannot be null");
        return UserProjectRole.builder()
                .userProject(userProject)
                .role(role)
                .build();
    }

    public List<UserProjectRoleResponse> listResponse(List<UserProjectRole> entities){
        return entities == null || entities.isEmpty() ? List.of()
                : entities.stream().map(this::toResponse).toList();
    }


}

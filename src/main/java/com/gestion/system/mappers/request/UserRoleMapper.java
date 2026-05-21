package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.UserRoleRequest;
import com.gestion.system.dto.response.UserRoleResponse;
import com.gestion.system.mappers.response.RolesMapper;
import com.gestion.system.model.entities.Roles;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRoleMapper {
    private final UserMapper userMapper;
    private final RolesMapper rolesMapper;

    public UserRoleMapper(UserMapper userMapper, RolesMapper rolesMapper) {
        this.userMapper = userMapper;
        this.rolesMapper = rolesMapper;
    }

    public UserRoleResponse toResponse(UserRole entity) {
        return entity == null ? null
                : UserRoleResponse.builder()
                .user(userMapper.toResponse(entity.getUser()))
                .role(rolesMapper.toResponse(entity.getRole()))
                .build();
    }

    public UserRole requestToEntity(User user, Roles role) {
        if (user == null || role == null ) throw new IllegalArgumentException("User and Roles are required");
        return UserRole.builder()
                .user(user)
                .role(role)
                .build();
    }

    public List<UserRoleResponse> listResponse (List<UserRole> list){
        return list == null || list.isEmpty() ? List.of()
                : list.stream().map(this::toResponse).toList();
    }


}

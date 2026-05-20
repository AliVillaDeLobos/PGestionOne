package com.gestion.system.mappers;

import com.gestion.system.dto.response.RolesResponse;
import com.gestion.system.model.entities.Roles;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RolesMapper {

    public RolesResponse toResponse(Roles role) {
        return role == null ? null
                : RolesResponse.builder()
                .id(role.getId())
                .role(role.getRoleName())
                .build();
    }

    public List<RolesResponse> toResponseList(List<Roles> roles) {
        return roles == null || roles.isEmpty() ? List.of()
                : roles.stream().map(this::toResponse).toList();
    }
}

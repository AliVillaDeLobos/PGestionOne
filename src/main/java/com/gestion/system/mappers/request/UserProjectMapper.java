package com.gestion.system.mappers.request;

import com.gestion.system.dto.audit.UserProjectAuditModel;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserProject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserProjectMapper {
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    public UserProjectMapper(UserMapper userMapper, ProjectMapper projectMapper) {
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
    }

    public UserProjectResponse toResponse(UserProject entity) {
        return entity == null ? null
                : UserProjectResponse.builder()
                .id(entity.getId())
                .user(userMapper.toResponse(entity.getUser()))
                .project(projectMapper.toResponse(entity.getProjects()))
                .build();
    }

    public List<UserProjectResponse> listResponse(List<UserProject> entity) {
        return entity == null || entity.isEmpty() ? List.of()
                : entity.stream().map(this::toResponse).toList();
    }

    public UserProject requestToEntity(User user, Projects projects) {
        if (user == null || projects == null)
            throw new IllegalArgumentException("User and Project requests can't be null");
        return UserProject.builder()
                .user(user)
                .projects(projects)
                .build();
    }

    public UserProjectAuditModel toAudit(UserProject entity) {
        if (entity == null)
            throw new IllegalArgumentException("UserProject can't be null");
        return UserProjectAuditModel.builder()
                .idUser(entity.getUser().getId())
                .emailUser(entity.getUser().getEmail())
                .idProject(entity.getProjects().getId())
                .nameProject(entity.getProjects().getName())
                .build();
    }
}

package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.UserProjectRequest;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.model.entities.Project;
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
                .project(projectMapper.toResponse(entity.getProject()))
                .build();
    }

    public List<UserProjectResponse> listResponse(List<UserProject> entity) {
        return entity == null || entity.isEmpty() ? List.of()
                : entity.stream().map(this::toResponse).toList();
    }

    public UserProject requestToEntity(User user, Project project) {
        if (user == null || project == null) throw new IllegalArgumentException("User and Project requests can't be null");
        return UserProject.builder()
                .user(user)
                .project(project)
                .build();
    }

}

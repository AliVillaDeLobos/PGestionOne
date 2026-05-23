package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.model.entities.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {
    private final ModelMapper mapper;

    public ProjectMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProjectResponse toResponse(Project project) {
        return project == null ? null : mapper.map(project, ProjectResponse.class);
    }

    public Project requestToEntity(ProjectRequest dto) {
        return dto == null ? null : mapper.map(dto, Project.class);
    }

    public List<ProjectResponse> listResponse (List<Project> projects) {
        return projects == null || projects.isEmpty() ?  List.of()
                : projects.stream().map(this::toResponse).toList();
    }

    public List<Project> listEntity(List<ProjectRequest> dtos) {
        return dtos == null || dtos.isEmpty() ? List.of()
                : dtos.stream().map(this::requestToEntity).toList();
    }
}

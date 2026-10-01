package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.model.entities.Projects;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {
    private final ModelMapper mapper;

    public ProjectMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ProjectResponse toResponse(Projects projects) {
        return projects == null ? null : mapper.map(projects, ProjectResponse.class);
    }

    public Projects requestToEntity(ProjectRequest dto) {
        return dto == null ? null : mapper.map(dto, Projects.class);
    }

    public List<ProjectResponse> listResponse (List<Projects> projects) {
        return projects == null || projects.isEmpty() ?  List.of()
                : projects.stream().map(this::toResponse).toList();
    }

    public List<Projects> listEntity(List<ProjectRequest> dtos) {
        return dtos == null || dtos.isEmpty() ? List.of()
                : dtos.stream().map(this::requestToEntity).toList();
    }

}

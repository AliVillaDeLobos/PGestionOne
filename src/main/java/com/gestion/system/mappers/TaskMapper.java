package com.gestion.system.mappers;

import com.gestion.system.dto.request.TaskRequestUpdate;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.model.entities.Tasks;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    private final ModelMapper mapper;
//    private final PorjectMapper projectMapper;

    public TaskMapper(ModelMapper mapper
//        ProjectMapper projectMapper
    ) {
        this.mapper = mapper;
//        this.projectMapper = projectMapper
    }

    public TaskResponse toResponse(Tasks task) {
        if (task == null) return null;
        TaskResponse dto = mapper.map(task, TaskResponse.class);
//        dto.setProject(projectMapper.toResponse(tesk.getPorject()));
        return dto;
    }

    public Tasks requestToEntity(TaskRequestUpdate taskRequest
//    , Project project
    ) {
//        if (project == null ) throw new IllegalArgumentException("Error inyectando el proyecto a 'Tasks'");
        if (taskRequest == null) return null;
    }
}

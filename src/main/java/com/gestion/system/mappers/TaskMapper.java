package com.gestion.system.mappers;

import com.gestion.system.dto.request.TaskRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.model.entities.Project;
import com.gestion.system.model.entities.Tasks;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {
    private final ModelMapper mapper;
    private final ProjectMapper projectMapper;

    public TaskMapper(ModelMapper mapper, ProjectMapper projectMapper) {
        this.mapper = mapper;
        this.projectMapper = projectMapper;
    }

    public TaskResponse toResponse(Tasks task) {
        if (task == null) return null;
        TaskResponse dto = mapper.map(task, TaskResponse.class);
        dto.setProject(projectMapper.toResponse(task.getProject()));
        return dto;
    }

    public Tasks requestToEntity(TaskRequest taskRequest, Project project
    ) {
        if (project == null ) throw new IllegalArgumentException("Error inyectando el proyecto a 'Tasks'");
        if (taskRequest == null) return null;
        Tasks entity = mapper.map(taskRequest, Tasks.class);
        entity.setProject(project);
        return entity;
    }

    public List<TaskResponse> listResponse(List<Tasks> tasks){
        return tasks == null || tasks.isEmpty() ? List.of()
                : tasks.stream().map(this::toResponse).toList();
    }

    public List<Tasks> listEntity(List<TaskRequest> tasksResponse, Project project){
        if (project == null ) throw new IllegalArgumentException("Error inyectando el proyecto a 'Tasks'");
        return tasksResponse == null || tasksResponse.isEmpty() ? List.of()
                : tasksResponse.stream().map(
                        task -> this.requestToEntity(task, project)).toList();
    }
}

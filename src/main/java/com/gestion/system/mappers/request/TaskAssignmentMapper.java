package com.gestion.system.mappers.request;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.request.update.TaskAssignmentUpdateRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskAssignmentMapper {
    private final ModelMapper mapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;


    public TaskAssignmentMapper(ModelMapper mapper, TaskMapper  taskMapper, UserMapper userMapper) {
        this.mapper = mapper;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    public TaskAssignmentResponse toResponse(TaskAssignment entity) {
        if (entity == null) return null;
        TaskAssignmentResponse dto = mapper.map(entity, TaskAssignmentResponse.class);
        dto.setTask(taskMapper.toResponse(entity.getTask()));
        dto.setUser(userMapper.toResponse(entity.getUser()));
        return dto;
    }

    public TaskAssignment requestToEntity(TaskAssignmentRequest request, Tasks task, User user) {
        if (task == null || user == null) throw new IllegalArgumentException("Task o User cannot be null in TaskAssignmentRequest.");
        return request == null ? null
                : TaskAssignment.builder()
                .task(task)
                .user(user)
                .projectRoles(request.getProjectRoles())
                .build();
    }

    public List<TaskAssignmentResponse> listResponse(List<TaskAssignment> entities) {
        return entities == null || entities.isEmpty() ? List.of()
                : entities.stream().map(this::toResponse).toList();
    }

    public TaskAssignment updateEntity(TaskAssignmentUpdateRequest request, TaskAssignment entity) {
        if (entity == null || request == null) throw new IllegalArgumentException("Entity o TaskAssignment request cannot be null in TaskAssignmentUpdateRequest.");
        entity.setProjectRoles(request.getProjectRoles());
        return entity;
    }
}

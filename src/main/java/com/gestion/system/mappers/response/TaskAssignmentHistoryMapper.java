package com.gestion.system.mappers.response;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Action;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskAssignmentHistoryMapper {
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public TaskAssignmentHistoryMapper(UserMapper userMapper, TaskMapper taskMapper) {
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    public TaskAssignmentHistoryResponse toResponse(TaskAssignmentHistory entity){
        return entity == null ? null : TaskAssignmentHistoryResponse.builder()
                .id(entity.getId())
                .task(taskMapper.toResponse(entity.getTask()))
                .userAssigned(userMapper.toResponse(entity.getUserAssigned()))
                .userAssignedBy(userMapper.toResponse(entity.getUserAssignedBy()))
                .action(entity.getAction())
                .actionDate(entity.getActionDate())
                .build();
    }

    public List<TaskAssignmentHistoryResponse> listResponse(List<TaskAssignmentHistory> entity){
        return entity == null || entity.isEmpty() ? List.of()
                : entity.stream().map(this::toResponse).toList();
    }

    public TaskAssignmentHistory taskAssignmentToHistory (TaskAssignment taskAssignment, User userAssignedBy, Action action){
        return TaskAssignmentHistory.builder()
                .task(taskAssignment.getTask())
                .userAssignedBy(userAssignedBy)
                .userAssigned(taskAssignment.getUser())
                .role(taskAssignment.getProjectRoles())
                .action(action)
                .build();
    }
}

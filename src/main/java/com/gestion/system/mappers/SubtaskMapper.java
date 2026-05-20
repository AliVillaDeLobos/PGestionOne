package com.gestion.system.mappers;

import com.gestion.system.dto.request.SubtaskRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.Tasks;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public class SubtaskMapper {
    private final ModelMapper mapper;
    private final TaskMapper taskMapper;

    public SubtaskMapper(ModelMapper mapper, TaskMapper taskMapper) {
        this.mapper = mapper;
        this.taskMapper = taskMapper;
    }

    public SubtaskResponse toResponse(Subtask subtask) {
        if (subtask == null ) return null;
        SubtaskResponse dto = mapper.map(subtask, SubtaskResponse.class);
        dto.setTask(taskMapper.toResponse(subtask.getTask()));
        return dto;
    }

    public Subtask requestToEntity(SubtaskRequest subtaskRequest, Tasks task) {
        return subtaskRequest == null || task == null ?  null
            : Subtask.builder()
                .name(subtaskRequest.getName())
                .description(subtaskRequest.getDescription())
                .task(task)
                .build();
    }

    public List<SubtaskResponse> listResponse (List<Subtask> subtasks) {
        return subtasks == null || subtasks.isEmpty() ? List.of()
                : subtasks.stream().map(this::toResponse).toList();
    }

    public List<Subtask> listRequest (List<SubtaskRequest> subtaskRequests,  Tasks task) {
        if (task == null) throw new IllegalArgumentException("Task cannot be null");
        return subtaskRequests == null || subtaskRequests.isEmpty()
                ? List.of() : subtaskRequests.stream().map(lisSub ->
                    requestToEntity(lisSub, task)).toList();
    }

}

package com.gestion.system.mappers;

import com.gestion.system.dto.request.SubtaskRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.Tasks;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface SubtaskMapperMap {

        // Subtask → Response
        @Mapping(target = "task", source = "task")
        SubtaskResponse toResponse(Subtask subtask);

        // List Subtask → Response
        List<SubtaskResponse> toResponseList(List<Subtask> subtasks);

        // Request → Entity (con relación externa)
//        @Mapping(target = "task", source = "task")
        Subtask toEntity(SubtaskRequest request);

        // List Request → Entity
        List<Subtask> toEntityList(List<SubtaskRequest> requests,
                                   @Context Tasks task);
    }


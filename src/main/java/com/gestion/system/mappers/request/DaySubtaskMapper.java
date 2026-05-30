package com.gestion.system.mappers.request;


import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.mappers.response.DayMapper;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.Subtask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DaySubtaskMapper {
    private final DayMapper dayMapper;
    private final SubtaskMapper subtaskMapper;

    public  DaySubtaskMapper (DayMapper dayMapper, SubtaskMapper subtaskMapper) {
        this.dayMapper = dayMapper;
        this.subtaskMapper = subtaskMapper;
    }

    public DaySubtaskResponse toResponse (DaySubtasks daySubtasks) {
        return daySubtasks == null ? null : DaySubtaskResponse.builder()
                .id(daySubtasks.getId())
                .status(daySubtasks.getStatus())
                .day(dayMapper.toResponse(daySubtasks.getDay()))
                .subtask(subtaskMapper.toResponse(daySubtasks.getSubtask()))
                .build();
    }

    public DaySubtasks requestToEntity (DaySubtaskRequest daySubtaskRequest, Day day, Subtask subtask) {
        if (subtask == null || day == null) throw new IllegalArgumentException("Subtask cannot be null");
        return daySubtaskRequest == null   ?  null
                : DaySubtasks.builder().day(day).subtask(subtask).build();
    }

    public List<DaySubtaskResponse> listResponse (List<DaySubtasks> daySubtasks) {
            return daySubtasks == null || daySubtasks.isEmpty() ? List.of()
                    : daySubtasks.stream().map(this::toResponse).toList();
    }

// La lista de request a entity se relega al servicio por la logica que conlleva
 }

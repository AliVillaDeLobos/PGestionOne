package com.gestion.system.mappers;


import com.gestion.system.dto.request.DaySubtaskRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.model.entities.DaysSubtasks;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.Subtask;
import org.modelmapper.ModelMapper;
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

    public DaySubtaskResponse toResponse (DaysSubtasks daysSubtasks) {
        return daysSubtasks == null ? null : DaySubtaskResponse.builder()
                .id(daysSubtasks.getId())
                .status(daysSubtasks.getStatus())
                .day(dayMapper.toResponse(daysSubtasks.getDay()))
                .subtask(subtaskMapper.toResponse(daysSubtasks.getSubtask()))
                .build();
    }

    public DaysSubtasks requestToEntity (DaySubtaskRequest daySubtaskRequest, Day day, Subtask subtask) {
        if (subtask == null) throw new IllegalArgumentException("Subtask cannot be null");
        return daySubtaskRequest == null || day == null  ?  null
                : DaysSubtasks.builder().day(day).subtask(subtask).build();
    }

    public List<DaySubtaskResponse> listResponse (List<DaysSubtasks> daysSubtasks) {
            return daysSubtasks == null || daysSubtasks.isEmpty() ? List.of()
                    : daysSubtasks.stream().map(this::toResponse).toList();
    }

// La lista de request a entity se relega al servicio por la logica que conlleva
 }

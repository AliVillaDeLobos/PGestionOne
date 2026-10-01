package com.gestion.system.mappers.request;


import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.response.DaysHoursResponse;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.DaysHours;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DaysHoursMapper {
    private final ModelMapper mapper;
    private final DaySubtaskMapper daySubtaskMapper;

    public DaysHoursMapper(ModelMapper mapper, DaySubtaskMapper daySubtaskMapper) {
        this.mapper = mapper;
        this.daySubtaskMapper = daySubtaskMapper;
    }

    public DaysHoursResponse toResponse(DaysHours daysHours) {
        if ( daysHours == null) return  null;
        DaysHoursResponse dto = mapper.map(daysHours, DaysHoursResponse.class);
        dto.setDaySubtask(daySubtaskMapper.toResponse(daysHours.getDaySubtask()));
        return dto;
    }

    public DaysHours requestToEntity(DaysHoursRequest request, DaySubtasks daySubtasks) {
        if (request == null || daySubtasks == null) {return null;}
        DaysHours daysHours = mapper.map(request,DaysHours.class);
        daysHours.setDaySubtask(daySubtasks);
        return daysHours;
    }

    public List<DaysHoursResponse> listResponse (List<DaysHours> daysHoursList) {
        return daysHoursList == null || daysHoursList.isEmpty() ? List.of()
                : daysHoursList.stream().map(this::toResponse).toList();
    }

    public List<DaysHours> listEntity (List<DaysHoursRequest> listRequest, DaySubtasks daySubtasks) {
        return listRequest == null || listRequest.isEmpty() ? List.of() :
                listRequest.stream().map(request -> requestToEntity(request, daySubtasks))
                        .toList();
    }
}

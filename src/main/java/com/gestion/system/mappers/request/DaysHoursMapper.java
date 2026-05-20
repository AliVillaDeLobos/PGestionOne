package com.gestion.system.mappers.request;


import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.response.DaysHoursResponse;
import com.gestion.system.model.entities.DaysHours;
import com.gestion.system.model.entities.DaysSubtasks;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DaysHoursMapper {
    private final ModelMapper mapper;

    public DaysHoursMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public DaysHoursResponse toResponse(DaysHours daysHours) {
        return daysHours == null ? null : mapper.map(daysHours, DaysHoursResponse.class);
    }

    public DaysHours requestToEntity(DaysHoursRequest request, DaysSubtasks daysSubtasks ) {
        if (request == null || daysSubtasks == null) {return null;}
        DaysHours daysHours = mapper.map(request,DaysHours.class);
        daysHours.setDaySubtask(daysSubtasks);
        return daysHours;
    }

    public List<DaysHoursResponse> listResponse (List<DaysHours> daysHoursList) {
        return daysHoursList == null || daysHoursList.isEmpty() ? List.of()
                : daysHoursList.stream().map(this::toResponse).toList();
    }

    public List<DaysHours> listEntity (List<DaysHoursRequest> listRequest, DaysSubtasks daysSubtasks) {
        return listRequest == null || listRequest.isEmpty() ? List.of() :
                listRequest.stream().map(request -> requestToEntity(request, daysSubtasks))
                        .toList();
    }
}

package com.gestion.system.mappers.response;

import com.gestion.system.dto.response.WeekResponse;
import com.gestion.system.model.entities.Week;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeekMapper {
    private final ModelMapper mapper;

    public WeekMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    //Entity a Response
    public WeekResponse toResponse(Week week) {
        if (week == null) {return null;}
        WeekResponse dto = mapper.map(week, WeekResponse.class);
        dto.setWeekNumber(week.getWeekNum());
        return dto;
    }

    //List de Response
    public List<WeekResponse> listResponse(List<Week> weeks) {
        return weeks == null ? List.of() : weeks.stream().map(this::toResponse).toList();
    }
}

package com.gestion.system.mappers;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.model.entities.Day;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DayMapper {
    private final ModelMapper mapper;
    private final WeekMapper weekMapper;

    public DayMapper(ModelMapper mapper, WeekMapper weekMapper) {
        this.mapper = mapper;
        this.weekMapper = weekMapper;
    }

    //Entity a Response
    public DayResponse toResponse(Day day){
        if (day == null ){return null;}
        DayResponse dto = mapper.map(day, DayResponse.class);
        dto.setWeek(weekMapper.toResponse(day.getWeek()));
        return dto;
    }

    //Lista de Response
    public List<DayResponse> toResponseList(List<Day> days){
        if (days == null) {return List.of();}
        return days.stream().map(this::toResponse).toList();
    }
}

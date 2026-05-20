package com.gestion.system.mappers.update;


import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.model.entities.DaysSubtasks;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface DaySubtaskUpdateMapper {

    void updateEntity(DaySubtaskUpdateRequest dto, @MappingTarget DaysSubtasks entity);
}
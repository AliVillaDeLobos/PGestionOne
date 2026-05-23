package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.DaysHoursUpdateRequest;
import com.gestion.system.model.entities.DaysHours;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface DaysHoursUpdateMapper {

     @Mapping(target = "id", ignore = true)
    DaysHours updateEntity(DaysHoursUpdateRequest dto, @MappingTarget DaysHours entity);
}

package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.model.entities.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(config = GlobalMapperConfig.class)
public interface TaskUpdateMapper {

     @Mapping(target = "id", ignore = true)
    Tasks updateEntity(TaskUpdateRequest dto, @MappingTarget Tasks entity);
}

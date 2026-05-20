package com.gestion.system.mappers;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.TaskRequestUpdate;
import com.gestion.system.model.entities.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(config = GlobalMapperConfig.class)
public interface TaskUpdateMapper {

    void updateEntity(@MappingTarget Tasks entity, TaskRequestUpdate dto);
}

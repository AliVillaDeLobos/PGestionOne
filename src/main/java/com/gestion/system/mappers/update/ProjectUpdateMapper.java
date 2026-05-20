package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.model.entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ProjectUpdateMapper {

    void updateEntity(ProjectUpdateRequest dto, @MappingTarget Project entity);
}

package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.audit.ProjectAuditModel;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.model.entities.Projects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ProjectUpdateMapper {

     @Mapping(target = "id", ignore = true)
     Projects updateEntity(ProjectUpdateRequest dto, @MappingTarget Projects entity);

     ProjectAuditModel toAudit(Projects entity);
}

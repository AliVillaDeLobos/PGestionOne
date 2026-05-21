package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.UserProjectUpdateRequest;
import com.gestion.system.model.entities.Project;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface UserProjectUpdateMapper {

     @Mapping(target = "user", source = "user")
     @Mapping(target = "project", source = "project")
     @Mapping(target = "id", ignore = true)
    UserProject updateEntity(@MappingTarget UserProject entity, User user, Project project);
}

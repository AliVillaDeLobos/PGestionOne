package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.TaskAssignmentUpdateRequest;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface TaskAssignmentUpdateMapper {

      @Mapping(target = "task", source = "task")
      @Mapping(target = "user", source = "user")
      @Mapping(target = "role", source = "dto.role")
      @Mapping(target = "id", ignore = true)
     TaskAssignment updateEntity(TaskAssignmentUpdateRequest dto, Tasks task,
                                      User user, @MappingTarget TaskAssignment entity);

}

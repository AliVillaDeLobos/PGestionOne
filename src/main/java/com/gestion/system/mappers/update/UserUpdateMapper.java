package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.audit.UserAuditModel;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface UserUpdateMapper {

     @Mapping(source = "firstLastName", target = "paternalLastName")
     @Mapping(source = "secondLastName", target = "maternalLastName")
     @Mapping(target = "id", ignore = true)
    User updateEntity(UserUpdateRequest dto, @MappingTarget User entity);

     @Mapping(source = "systemRole", target = "role")
    UserAuditModel toAudit(User  user);
}

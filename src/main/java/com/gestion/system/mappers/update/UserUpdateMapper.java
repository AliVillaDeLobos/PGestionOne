package com.gestion.system.mappers.update;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.UserUpdateRequest;
import com.gestion.system.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface UserUpdateMapper {

    void updateEntity(UserUpdateRequest dto, @MappingTarget User entity);
}

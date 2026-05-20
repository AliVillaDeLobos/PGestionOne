package com.gestion.system.mappers.update;


import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.model.entities.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface SubtaskUpdateMapper {

        void updateEntity(SubtaskUpdateRequest dto, @MappingTarget Subtask entity);

    }


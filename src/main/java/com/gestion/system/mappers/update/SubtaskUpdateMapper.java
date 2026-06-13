package com.gestion.system.mappers.update;


import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.audit.SubtaskAuditModel;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.model.entities.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface SubtaskUpdateMapper {

     @Mapping(target = "id", ignore = true)
     @Mapping(target = "task", ignore = true)
    Subtask updateEntity(SubtaskUpdateRequest dto, @MappingTarget Subtask entity);

     @Mapping(source = "task.id", target = "idTask")
     @Mapping(source = "task.name", target = "nameTask")
    SubtaskAuditModel toAudit(Subtask subtask);

}


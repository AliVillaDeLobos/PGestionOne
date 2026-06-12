package com.gestion.system.mappers.update;


import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.audit.DaySubtaskAuditModel;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.DaySubtasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface DaySubtaskUpdateMapper {

      @Mapping(target = "id", ignore = true)
     DaySubtasks updateEntity(DaySubtaskUpdateRequest dto, Day day, @MappingTarget DaySubtasks entity);

      @Mapping(source = "subtask.id", target = "idSubtask")
      @Mapping(source = "subtask.name", target = "nameSubtask")
      @Mapping(source = "day.id", target = "idDay")
      @Mapping(source = "status", target = "status")
     DaySubtaskAuditModel toAudit(DaySubtasks entity);
}
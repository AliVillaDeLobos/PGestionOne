package com.gestion.system.mappers.response;

import com.gestion.system.config.GlobalMapperConfig;
import com.gestion.system.dto.response.SubtaskDeletedHistoryResponse;
import com.gestion.system.model.entities.SubtaskDeletedHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface SubtaskDeletedHistoryMapper {

    SubtaskDeletedHistoryResponse toResponse(SubtaskDeletedHistory entity);
    List<SubtaskDeletedHistoryResponse> listResponse(List<SubtaskDeletedHistory> entity);
}

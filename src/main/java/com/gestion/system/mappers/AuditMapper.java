package com.gestion.system.mappers;

import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.model.entities.Audit;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditMapper {
    private UserMapper userMapper;

    public AuditMapper( UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //Entidad a DTO
    public AuditResponse toResponse(Audit audit) {
        if (audit == null) {return null;}
        AuditResponse dto = new AuditResponse();
            dto.setId(audit.getIdAudit());
            dto.setTableName(audit.getTableName());
            dto.setRecordId(audit.getRecordId());
            dto.setAction(audit.getOperation());
            dto.setCreatedAt(audit.getCreatedDate());

        // Es mejor por el tipo de dato hacerlo manual
            dto.setOldData(audit.getOldData());
            dto.setNewData(audit.getNewData());

            dto.setCreatedBy(
                userMapper.toResponse(audit.getUserCreated()));
        return dto;
    }

    //Lista de Entity a DTO
    public List<AuditResponse> listResponse(List<Audit> audits) {
        if (audits == null || audits.isEmpty()) {return List.of();}
        return audits.stream().map(this::toResponse).collect(Collectors.toList());
    }


}

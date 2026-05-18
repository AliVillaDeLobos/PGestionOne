package com.gestion.system.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.gestion.system.model.enums.Operation;
import lombok.*;


import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditResponse {


    private Integer id;
    private String tableName;
    private Integer recordId;
    private Operation action;
    private UserResponse createdBy;
    private JsonNode oldData;
    private JsonNode newData;
    private LocalDateTime createdAt;

}

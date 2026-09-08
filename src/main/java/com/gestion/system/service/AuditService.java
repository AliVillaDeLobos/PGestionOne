package com.gestion.system.service;


import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Operation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {

    void delete(AuditableEntity table, Integer recordId, User actor, Object oldData);
    void update(AuditableEntity table, Integer recordId, User actor, Object oldData, Object newData);
    void create (AuditableEntity table, Integer recordId, User actor, Object newData);
    void restore(AuditableEntity table, Integer recordId, User actor, Object newData);

    List<AuditResponse> getAllByUser(Integer idUser,  Integer actor);
    List<AuditResponse> getAllByTable(Integer idUser, AuditableEntity table);
    List<AuditResponse> getAllByRecord(Integer idUser, Integer recordId);
    List<AuditResponse> getAllOperationByDate(Integer idUser, Operation operation, LocalDate startDate, LocalDate endDate);


}

package com.gestion.system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.system.dto.response.AuditResponse;
import com.gestion.system.mappers.response.AuditMapper;
import com.gestion.system.model.entities.Audit;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Operation;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.AuditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;
    private final AuditMapper auditMapper;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional
    public void delete(AuditableEntity table, Integer recordId, User actor, Object oldData) {
        JsonNode json = objectMapper.valueToTree(oldData);
        Audit audit = Audit.builder()
                .tableName(table.name())
                .recordId(recordId)
                .operation(Operation.DELETE)
//                .createdDate(LocalDateTime.now()) //Ya esta como DEFAULT en DB
                .userCreated(actor)
                .oldData(json)
                .build();
        auditRepository.save(audit);
    }

    @Override
    public void update(AuditableEntity table, Integer recordId, User actor, Object oldData, Object newData) {
        JsonNode json = objectMapper.valueToTree(oldData);
        JsonNode newJson = objectMapper.valueToTree(newData);
        Audit audit = Audit.builder()
                .tableName(table.name())
                .recordId(recordId)
                .operation(Operation.UPDATE)
                .userCreated(actor)
                .oldData(json)
                .newData(newJson)
                .build();
        auditRepository.save(audit);
    }

    @Override
    @Transactional
    public void create(AuditableEntity table, Integer recordId, User actor, Object newData) {
        JsonNode json = objectMapper.valueToTree(newData);
        Audit audit = Audit.builder()
                .tableName(table.name())
                .recordId(recordId)
                .userCreated(actor)
                .operation(Operation.CREATE)
                .newData(json)
                .build();
        auditRepository.save(audit);
    }

    @Override
    @Transactional
    public void restore(AuditableEntity table, Integer recordId, User actor, Object newData) {
        JsonNode json = objectMapper.valueToTree(newData);
        Audit audit = Audit.builder()
                .tableName(table.name())
                .recordId(recordId)
                .userCreated(actor)
                .operation(Operation.RESTORE)
                .newData(json)
                .build();
        auditRepository.save(audit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponse> getAllByUser(Integer idUser, Integer idActor) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);

        return auditMapper.listResponse(auditRepository.findAllByUserCreated_Id(idActor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponse> getAllByTable(Integer idUser, AuditableEntity table) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        return auditMapper.listResponse(auditRepository.findAllByTableNameIgnoreCase(table.name()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponse> getAllByRecord(Integer idUser, Integer recordId) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        return auditMapper.listResponse(auditRepository.findAllByRecordId(recordId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditResponse> getAllOperationByDate(Integer idUser,Operation operation, LocalDate startDate, LocalDate endDate){
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);

        return auditMapper.listResponse(auditRepository.findAllByOperationAndCreatedDateBetween(
                operation,  startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()));
    }
}


package com.gestion.system.repositories;

import com.gestion.system.model.entities.Audit;
import com.gestion.system.model.enums.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AuditRepository extends JpaRepository<Audit,Integer> {

    List<Audit> findAllByOperationAndCreatedDateBetween(Operation operation, LocalDateTime min, LocalDateTime max);
    List<Audit> findAllByUserCreated_Id(Integer idUserCreated);

    List<Audit> findAllByTableNameIgnoreCase(String tableName);
    List<Audit> findAllByRecordId(Integer recordId);
}

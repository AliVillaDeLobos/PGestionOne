package com.gestion.system.service;


import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;

public interface AuditService {

    void delete(AuditableEntity table, Integer recordId, User actor, Object oldData);
    void update(AuditableEntity table, Integer recordId, User actor, Object oldData, Object newData);
    void create (AuditableEntity table, Integer recordId, User actor, Object newData);
    void restore(AuditableEntity table, Integer recordId, User actor, Object newData);
}

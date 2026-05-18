package com.gestion.system.repositories;

import com.gestion.system.model.entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit,Integer> {
}

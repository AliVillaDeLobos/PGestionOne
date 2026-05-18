package com.gestion.system.repositories;

import com.gestion.system.model.entities.SubtaskDeletedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskDeletedHistoryRepository extends JpaRepository<SubtaskDeletedHistory,Integer> {
}

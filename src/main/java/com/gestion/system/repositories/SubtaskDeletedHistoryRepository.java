package com.gestion.system.repositories;

import com.gestion.system.model.entities.SubtaskDeletedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubtaskDeletedHistoryRepository extends JpaRepository<SubtaskDeletedHistory,Integer> {

    Optional<SubtaskDeletedHistory> findBySubtask_Id(Integer subtask_id);
}

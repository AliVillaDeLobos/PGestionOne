package com.gestion.system.repositories;

import com.gestion.system.model.entities.TaskAssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentHistoryRepository extends JpaRepository<TaskAssignmentHistory,Integer> {
}

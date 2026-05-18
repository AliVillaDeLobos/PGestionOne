package com.gestion.system.repositories;

import com.gestion.system.model.entities.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Integer> {
}

package com.gestion.system.repositories;

import com.gestion.system.model.entities.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<Subtask,Integer> {
}

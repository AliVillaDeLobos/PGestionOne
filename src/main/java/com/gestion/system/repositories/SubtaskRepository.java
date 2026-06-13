package com.gestion.system.repositories;

import com.gestion.system.model.entities.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask,Integer> {

    List<Subtask> findAllByTask_IdAndIsDeletedFalse(Integer idTask);
    List<Subtask> findAllByIsDeleted(Boolean isDeleted);
    List<Subtask> findAllByCompleted(Boolean completed);
}

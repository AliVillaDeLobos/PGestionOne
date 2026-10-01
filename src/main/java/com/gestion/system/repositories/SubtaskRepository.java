package com.gestion.system.repositories;

import com.gestion.system.model.entities.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<Subtask,Integer> {

    List<Subtask> findAllByTask_IdAndIsDeletedFalse(Integer idTask);
    List<Subtask> findAllByTask_IdAndIsDeleted(Integer idTask, Boolean isDeleted);
    List<Subtask> findAllByTask_IdAndCompleted(Integer idTask,Boolean completed);
    Optional<Subtask> findByIdAndTask_Id(Integer idSubtask, Integer idTask);
}

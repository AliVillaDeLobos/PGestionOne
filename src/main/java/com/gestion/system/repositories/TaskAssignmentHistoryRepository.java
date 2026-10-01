package com.gestion.system.repositories;

import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.enums.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssignmentHistoryRepository extends JpaRepository<TaskAssignmentHistory,Integer> {

    @Query("""
        SELECT tah FROM TaskAssignmentHistory tah
        WHERE tah.task.projects.id = :idProject
        AND tah.action = :action
        ORDER BY tah.actionDate DESC
    """)
    List<TaskAssignmentHistory> findAllByProjectAndAction(@Param("idProject")Integer idProject,
                                                          @Param("action") Action action);
    @Query("""
        SELECT tah FROM TaskAssignmentHistory tah
        WHERE tah.task.id = :idTask
        AND tah.action = :action
        ORDER BY tah.actionDate DESC
    """)
    List<TaskAssignmentHistory> findAllByTaskAndAction(@Param("idTask")Integer idTask,
                                                       @Param("action") Action action);
    @Query("""
        SELECT tah FROM TaskAssignmentHistory tah
        WHERE tah.userAssigned.id = :idUser
        AND tah.action = :action
        ORDER BY tah.actionDate DESC
    """)
    List<TaskAssignmentHistory> findAllByUserAndAction(@Param("idUser")Integer idUser,
                                                       @Param("action") Action action);
}

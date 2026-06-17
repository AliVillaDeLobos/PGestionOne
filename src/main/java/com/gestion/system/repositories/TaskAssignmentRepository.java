package com.gestion.system.repositories;

import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.enums.ProjectRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.config.Task;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Integer> {


    @Query("""
        SELECT ta FROM TaskAssignment ta
        WHERE ta.task.projects.id = :idProject
    """)
    List<TaskAssignment> findAllByProject(@Param("idProject") Integer idProject);

    @Query("""
        SELECT ta FROM TaskAssignment ta
        WHERE ta.user.id = :idUser
    """)
    List<TaskAssignment> findAllByUser(@Param("idUser") Integer idUser);

    @Query("""
        SELECT ta FROM TaskAssignment ta
        WHERE ta.task.id = :idTask
    """)
    List<TaskAssignment> findAllByTask(@Param("idTask") Integer idTask);


    Integer id(Integer id);
}

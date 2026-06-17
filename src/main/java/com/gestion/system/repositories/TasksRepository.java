package com.gestion.system.repositories;

import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import org.modelmapper.internal.util.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks,Integer> {

    List<Tasks> findAllByProjects_Id(Integer idProject);
    Optional<Tasks> findByIdAndProjects_Id(Integer idTask, Integer idProject);
    List<Tasks> findAllByProjects_IdAndNameContainingIgnoreCase(Integer idProject, String name);
    List<Tasks> findAllByProjects_IdAndColor(Integer projectsId, Colors color);
    List<Tasks> findAllByProjects_IdAndStatus(Integer projectsId, Status status);

    @Query("""
        SELECT t FROM Tasks t
        WHERE t.projects.id = :projectId
        AND t.startDate <= :today
        AND t.endDate >= :today
    """)
    List<Tasks> findActiveTask(@Param("projectId") Integer idProject,
                               @Param("today") LocalDate today);

    @Query("""
        SELECT t FROM Tasks T
        WHERE t.projects.id = :projectId
        AND t.startDate <= :end
        AND t.endDate >= :start
    """)
    List<Tasks> findBetweenDates(@Param("projectId") Integer idProject,
                                 @Param("start") LocalDate startDate,
                                 @Param("end") LocalDate endDate);

    Integer id(Integer id);
}

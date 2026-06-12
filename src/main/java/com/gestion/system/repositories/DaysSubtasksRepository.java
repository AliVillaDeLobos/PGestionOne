package com.gestion.system.repositories;

import com.gestion.system.model.entities.DaySubtasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaysSubtasksRepository extends JpaRepository<DaySubtasks,Integer> {

    List<DaySubtasks> findAllBySubtask_Id(Integer subtaskId);
    List<DaySubtasks> findAllByDay_Id(Integer dayId);
}

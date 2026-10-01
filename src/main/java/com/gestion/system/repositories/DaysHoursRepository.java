package com.gestion.system.repositories;

import com.gestion.system.model.entities.DaysHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaysHoursRepository extends JpaRepository<DaysHours,Integer> {

    List<DaysHours> findAllByDaySubtask_Id(Integer daySubtaskId);

}


package com.gestion.system.repositories;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.model.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaysRepository extends JpaRepository<Day,Integer> {

    List<Day> findByWeek_Id(Integer weekId);
    Optional<Day> findByDayNameAndWeek_Id(String dayName, Integer weekId);

}

package com.gestion.system.repositories;

import com.gestion.system.model.entities.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaysRepository extends JpaRepository<Day,Integer> {
}

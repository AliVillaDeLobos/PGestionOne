package com.gestion.system.repositories;

import com.gestion.system.model.entities.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeksRepository extends JpaRepository<Week,Integer> {

}

package com.gestion.system.repositories;

import com.gestion.system.model.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks,Integer> {
}

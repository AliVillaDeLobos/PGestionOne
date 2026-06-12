package com.gestion.system.repositories;

import com.gestion.system.model.entities.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Projects,Integer> {
    List<Projects> findByNameContainsIgnoreCase(String name);
}

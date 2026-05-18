package com.gestion.system.repositories;

import com.gestion.system.model.entities.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject,Integer> {
}

package com.gestion.system.repositories;

import com.gestion.system.model.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Integer> {
}

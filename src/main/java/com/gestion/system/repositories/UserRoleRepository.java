package com.gestion.system.repositories;

import com.gestion.system.model.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
}

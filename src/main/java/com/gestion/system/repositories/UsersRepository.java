package com.gestion.system.repositories;

import com.gestion.system.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,Integer> {
}

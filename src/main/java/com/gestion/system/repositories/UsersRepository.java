package com.gestion.system.repositories;

import com.gestion.system.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Integer> {

    Optional<User> findById(Integer id);
    Optional<User> findByEmail(String email);
}

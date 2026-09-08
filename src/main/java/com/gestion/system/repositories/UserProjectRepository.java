package com.gestion.system.repositories;

import com.gestion.system.model.entities.UserProject;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectRepository extends JpaRepository<UserProject,Integer> {

    Boolean existsByUser_IdAndProjects_Id(Integer idUser, Integer idProject);
    List<UserProject> findAllByProjects_Id(Integer projectsId);
    List<UserProject> findAllByUser_Id(Integer usersId);
}

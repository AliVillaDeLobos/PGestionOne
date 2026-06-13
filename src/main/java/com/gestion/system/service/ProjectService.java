package com.gestion.system.service;

import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.model.entities.Projects;

import java.util.List;

public interface ProjectService {

    ProjectResponse getById(Integer idProject);
    List<ProjectResponse> searchByName(String name);
    List<ProjectResponse> getAll();
    ProjectResponse create(ProjectRequest projectRequest, Integer idUser);
    ProjectResponse update(Integer idProject, ProjectUpdateRequest projectRequest, Integer idUser);
    void delete(Integer idProject, Integer idUser);
    Projects findProject(Integer idProject);
}

package com.gestion.system.service;

import com.gestion.system.dto.request.create.UserProjectRequest;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.model.entities.UserProject;

import java.util.List;

public interface UserProjectService {

    UserProjectResponse create(Integer idUser, UserProjectRequest request);
    void delete(Integer idUser, Integer idUserProject);

    UserProjectResponse getById(Integer idUser, Integer idUserProject);
    List<UserProjectResponse> getAllProjectsByUser(Integer idUserRequester, Integer idUserRequest);
    List<UserProjectResponse> getAllUsersByProjects(Integer idUser, Integer idProject);
    UserProject findUserProject(Integer idUserProject);

}

package com.gestion.system.service;

import com.gestion.system.dto.request.create.UserProjectRequest;
import com.gestion.system.dto.response.UserProjectResponse;
import com.gestion.system.exceptions.BusinessRulesExceptions.DuplicateAssignmentException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.UserProjectMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.entities.UserProject;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UserProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {
    private final UserProjectRepository userProjectRepository;
    private final UserProjectMapper userProjectMapper;
    private final ProjectService projectService;
    private final UserService userService;
    private final AuditService audit;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional
    public UserProjectResponse create(Integer idUser, UserProjectRequest request) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        User userAssign = userService.findUser(request.getUserId());
        Projects project = projectService.findProject(request.getProjectId());

        if (userProjectRepository.existsByUser_IdAndProjects_Id(userAssign.getId(), project.getId()))
            throw new DuplicateAssignmentException();

        UserProject userProject = userProjectMapper.requestToEntity(userAssign, project);
        userProject = userProjectRepository.save(userProject);

        audit.create(AuditableEntity.USER_PROJECT, userProject.getId(), user, userProjectMapper.toAudit(userProject));
        return userProjectMapper.toResponse(userProject);
    }

    @Override
    @Transactional
    public void delete(Integer idUser, Integer idUserProject) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        UserProject userProject = findUserProject(idUserProject);

        audit.delete(AuditableEntity.USER_PROJECT, userProject.getId(), user, userProjectMapper.toAudit(userProject));
        userProjectRepository.delete(userProject);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProjectResponse getById(Integer idUser, Integer idUserProject) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        return userProjectMapper.toResponse(findUserProject(idUserProject));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProjectResponse> getAllProjectsByUser(Integer idUserRequester, Integer idUserRequest) {
        userAuthorization.authorizeUser(idUserRequester, SystemRole.ADMIN);
        return userProjectMapper.listResponse(userProjectRepository.findAllByUser_Id(idUserRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProjectResponse> getAllUsersByProjects(Integer idUser, Integer idProject) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        return userProjectMapper.listResponse(userProjectRepository.findAllByProjects_Id(idProject));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProject findUserProject(Integer idUserProject) {
        return userProjectRepository.findById(idUserProject).orElseThrow(
                () -> new ResourceNotFoundException("User Project not found with ID: " + idUserProject));
    }
}

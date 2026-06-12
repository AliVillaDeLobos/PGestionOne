package com.gestion.system.service;

import com.gestion.system.dto.audit.ProjectAuditModel;
import com.gestion.system.dto.request.create.ProjectRequest;
import com.gestion.system.dto.request.update.ProjectUpdateRequest;
import com.gestion.system.dto.response.ProjectResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.ProjectMapper;
import com.gestion.system.mappers.update.ProjectUpdateMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectUpdateMapper projectUpdateMapper;

    private final UserAuthorizationService userAuthorization;
    private final AuditService audit;

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getById(Integer idProject) {
        Projects project = findProject(idProject);

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> searchByName(String name) {
        return projectMapper.listResponse(
                projectRepository.findByNameContainsIgnoreCase(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAll() {
        return projectMapper.listResponse(
                projectRepository.findAll());
    }

    @Override
    @Transactional
    public ProjectResponse create(ProjectRequest projectRequest, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        Projects project = projectMapper.requestToEntity(projectRequest);
        Projects saved = projectRepository.save(project);
        audit.create(AuditableEntity.PROJECT, saved.getId(), user, projectUpdateMapper.toAudit(saved));
        return projectMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProjectResponse update(Integer idProject, ProjectUpdateRequest projectRequest, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Projects project = findProject(idProject);

        ProjectAuditModel original = projectUpdateMapper.toAudit(project);
        projectUpdateMapper.updateEntity(projectRequest, project);
        projectRepository.save(project);

        audit.update(AuditableEntity.PROJECT, project.getId(), user, original, projectUpdateMapper.toAudit(project));

        return projectMapper.toResponse(project);
    }

    @Override
    @Transactional
    public void delete(Integer idProject, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        Projects project = findProject(idProject);

        audit.delete(AuditableEntity.PROJECT, project.getId(), user, projectUpdateMapper.toAudit(project));
        projectRepository.delete(project);
    }

    private Projects findProject(Integer idProject){
        return projectRepository.findById(idProject).orElseThrow(
                () -> new ResourceNotFoundException("Project not found with ID: " + idProject));
    }

}

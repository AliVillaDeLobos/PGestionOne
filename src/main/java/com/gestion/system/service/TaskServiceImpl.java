package com.gestion.system.service;

import com.gestion.system.dto.audit.TaskAuditModel;
import com.gestion.system.dto.request.create.TaskRequest;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.exceptions.InvalidObjectModificationException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.TaskMapper;
import com.gestion.system.mappers.update.TaskUpdateMapper;
import com.gestion.system.model.entities.Projects;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TasksRepository tasksRepository;
    private final TaskMapper taskMapper;
    private final TaskUpdateMapper taskUpdateMapper;
    private final ProjectService projectService;
    private final AuditService audit;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByProjectId(Integer idProject) {
        return taskMapper.listResponse(
                tasksRepository.findAllByProjects_Id(idProject));
    }

//    Si los parametros de busqueda crecen en lugar de hacer mas metodos, mejor mover a un DTO que filtre
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByNameContaining(Integer idProject, String name) {
        return taskMapper.listResponse(
                tasksRepository.findAllByProjects_IdAndNameContainingIgnoreCase(idProject, name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByColor(Integer idProject, Colors color) {
        return taskMapper.listResponse(
                tasksRepository.findAllByProjects_IdAndColor(idProject, color));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getByStatus(Integer idProject, Status status) {
        return taskMapper.listResponse(
                tasksRepository.findAllByProjects_IdAndStatus(idProject, status));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Integer id) {
        return taskMapper.toResponse(findTask(id));
    }

    @Override
    @Transactional
    public TaskResponse create(Integer idUser, TaskRequest taskRequest, Integer idProject) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Projects project = projectService.findProject(idProject);

        Tasks task = taskMapper.requestToEntity(taskRequest, project);
        tasksRepository.save(task);

        audit.create(AuditableEntity.TASKS, task.getId(), user, taskUpdateMapper.toAudit(task));
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse update(Integer idUser, TaskUpdateRequest update, Integer idProject, Integer idTask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MEMBER);
        Tasks task =tasksRepository.findByIdAndProjects_Id(idTask, idProject).orElseThrow(
                () -> new ResourceNotFoundException("Task no found in project."));
        TaskAuditModel old = taskUpdateMapper.toAudit(task);

        taskUpdateMapper.updateEntity(update, task);
        tasksRepository.save(task);

        audit.update(AuditableEntity.TASKS, task.getId(), user, old, taskUpdateMapper.toAudit(task));

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public void delete(Integer idUser, Integer idProject, Integer idTask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Projects project = projectService.findProject(idProject);
        Tasks task =tasksRepository.findByIdAndProjects_Id(idTask, idProject).orElseThrow(
                () -> new ResourceNotFoundException("Task no found in project."));

        audit.delete(AuditableEntity.TASKS, task.getId(), user, taskUpdateMapper.toAudit(task));
        tasksRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> activeList(Integer idProject) {
        return taskMapper.listResponse(tasksRepository.findActiveTask(idProject, LocalDate.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> betweenDates(Integer idProject, LocalDate startDate, LocalDate endDate) {
        return taskMapper.listResponse(tasksRepository.findBetweenDates(idProject, startDate, endDate));
    }



    @Override
    @Transactional(readOnly = true)
    public Tasks findTask(Integer idTask) {
        return tasksRepository.findById(idTask).orElseThrow(
                () -> new ResourceNotFoundException("Task not found with ID: "+idTask));
    }


}

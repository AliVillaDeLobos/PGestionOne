package com.gestion.system.service;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.TaskAssignmentMapper;
import com.gestion.system.mappers.update.TaskUpdateMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskAssignmentServiceImpl implements TaskAssignmentService{
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final TaskAssignmentMapper taskAssignmentMapper;
    private final TaskAssignmentHistoryService historyService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;

    private final UserAuthorizationService userAuthorization;


    @Override
    @Transactional
    public TaskAssignmentResponse assign(Integer idUser, TaskAssignmentRequest request) {
        User manager = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
//        User member = userAuthorization.authorizeUser(request.getUserId(), SystemRole.MEMBER);
        User member = userService.findUser(request.getUserId());
        Tasks task = taskService.findTask(request.getTaskId());

        TaskAssignment taskAssignment = taskAssignmentMapper.requestToEntity(request, task, member);
        taskAssignment = taskAssignmentRepository.save(taskAssignment);
        historyService.assign(taskAssignment, manager);

        return taskAssignmentMapper.toResponse(taskAssignment);
    }

    @Override
    @Transactional
    public void unassign(Integer idUser, Integer idTaskAssignment) {
        User manager = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        TaskAssignment taskAssignment = taskAssignmentRepository.findById(idTaskAssignment).orElseThrow(
                () -> new ResourceNotFoundException("Task Assigment not found with ID: " + idTaskAssignment));

        historyService.unassign(taskAssignment, manager);
        taskAssignmentRepository.delete(taskAssignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentResponse> getAllByProject(Integer idUser,Integer idProject) {
        userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        projectService.findProject(idProject);

        return taskAssignmentMapper.listResponse(taskAssignmentRepository.findAllByProject(idProject));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentResponse> getAllByUser(Integer idUser, Integer idUserRequest) {
        userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        userAuthorization.authorizeUser(idUserRequest, SystemRole.MEMBER);

        return taskAssignmentMapper.listResponse(taskAssignmentRepository.findAllByUser(idUserRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentResponse> getAllByTask(Integer idUser, Integer idTask) {
        userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        taskService.findTask(idTask);

        return taskAssignmentMapper.listResponse(taskAssignmentRepository.findAllByTask(idTask));
    }
}

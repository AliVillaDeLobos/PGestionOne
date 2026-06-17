package com.gestion.system.service;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.mappers.response.TaskAssignmentHistoryMapper;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.TaskAssignmentHistory;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Action;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.TaskAssignmentHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskAssignmentHistoryServiceImpl implements TaskAssignmentHistoryService {
    private final TaskAssignmentHistoryRepository historyRepository;
    private final TaskAssignmentHistoryMapper historyMapper;
    private final TaskService taskService;
    private final ProjectService projectService;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional
    public void assign(TaskAssignment taskAssignment, User userAssignedBy) {
       saveHistory(taskAssignment, userAssignedBy, Action.ASSIGNED);
    }

    @Override
    @Transactional
    public void unassign(TaskAssignment taskAssignment, User userAssignedBy) {
        saveHistory(taskAssignment, userAssignedBy, Action.UNASSIGNED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentHistoryResponse> getAllByTaskAndAction(Integer idUser, Integer idTask, Action action) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        taskService.findTask(idTask);

        return historyMapper.listResponse(historyRepository.findAllByTaskAndAction(idTask, action));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentHistoryResponse> getAllByUserAndAction(Integer idUser, Integer idUserRequest, Action action) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        userAuthorization.authorizeUser(idUserRequest, SystemRole.MEMBER);

        return historyMapper.listResponse(historyRepository.findAllByUserAndAction(idUserRequest, action));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskAssignmentHistoryResponse> getAllByProjectAndAction(Integer idUser, Integer idProject, Action action) {
        userAuthorization.authorizeUser(idUser, SystemRole.ADMIN);
        projectService.findProject(idProject);

        return historyMapper.listResponse(historyRepository.findAllByProjectAndAction(idProject, action));
    }

    private void saveHistory(TaskAssignment taskAssignment, User userAssignedBy, Action action) {
        TaskAssignmentHistory history = historyMapper.taskAssignmentToHistory(taskAssignment, userAssignedBy, action);
        historyRepository.saveAndFlush(history);
    }
}

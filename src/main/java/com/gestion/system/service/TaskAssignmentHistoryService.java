package com.gestion.system.service;

import com.gestion.system.dto.response.TaskAssignmentHistoryResponse;
import com.gestion.system.model.entities.TaskAssignment;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.Action;

import java.util.List;

public interface TaskAssignmentHistoryService {

    void assign(TaskAssignment taskAssignment, User userAssignedBy);
    void unassign(TaskAssignment  taskAssignment, User userAssignedBy);

    List<TaskAssignmentHistoryResponse> getAllByTaskAndAction(Integer idUser, Integer idTask, Action action);
    List<TaskAssignmentHistoryResponse> getAllByUserAndAction(Integer idUser, Integer idUserRequest, Action action);
    List<TaskAssignmentHistoryResponse> getAllByProjectAndAction(Integer idUser, Integer idProject, Action action);


}

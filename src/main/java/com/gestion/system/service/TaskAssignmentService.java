package com.gestion.system.service;

import com.gestion.system.dto.request.create.TaskAssignmentRequest;
import com.gestion.system.dto.response.TaskAssignmentResponse;

import java.util.List;

public interface TaskAssignmentService {

    TaskAssignmentResponse assign(Integer idUser, TaskAssignmentRequest request);
    void unassign(Integer idUser, Integer idTaskAssignment);
    List<TaskAssignmentResponse> getAllByProject(Integer idUser, Integer idProject);
    List<TaskAssignmentResponse> getAllByUser(Integer idUser, Integer idUserRequest);
    List<TaskAssignmentResponse> getAllByTask(Integer idUser, Integer idTask);

}

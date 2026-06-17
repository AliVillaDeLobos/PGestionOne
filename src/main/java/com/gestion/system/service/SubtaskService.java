package com.gestion.system.service;

import com.gestion.system.dto.request.create.SubtaskRequest;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.model.entities.Subtask;

import java.util.List;

public interface SubtaskService {

    SubtaskResponse getById(int idSubtask);
    List<SubtaskResponse> getAllByTaskId(Integer idTask);
    List<SubtaskResponse> getAllIsDeleted(Integer idTask);
    List<SubtaskResponse> getAllNotCompleted(Integer idTask);
    List<SubtaskResponse> getAllCompleted(Integer idTask);
    SubtaskResponse create(Integer idUser, SubtaskRequest request, Integer idTask);
    SubtaskResponse update(Integer idUser, Integer idSubtask, SubtaskUpdateRequest update, Integer idTask);
    SubtaskResponse delete(Integer idUser, Integer idSubtask, String message, Integer idTask);
    SubtaskResponse restore(Integer idUser, Integer idSubtask);
    Subtask findSubtask(Integer idSubtask);
}

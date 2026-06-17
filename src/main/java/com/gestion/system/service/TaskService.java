package com.gestion.system.service;

import com.gestion.system.dto.request.create.TaskRequest;
import com.gestion.system.dto.request.update.TaskUpdateRequest;
import com.gestion.system.dto.response.TaskResponse;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    List<TaskResponse> getByProjectId(Integer idProject);
    List<TaskResponse> getByNameContaining(Integer idProject, String name);
    List<TaskResponse> getByColor(Integer idProject, Colors color);
    List<TaskResponse> getByStatus(Integer idProject, Status status);
    TaskResponse getById(Integer id);
    TaskResponse create(Integer idUser, TaskRequest taskRequest, Integer idProject);
    TaskResponse update(Integer idUser, TaskUpdateRequest update, Integer idProject, Integer idTask);
    void delete(Integer idUser, Integer idProject, Integer idTask);
    List<TaskResponse> activeList(Integer idProject);
    List<TaskResponse> betweenDates(Integer idProject, LocalDate startDate, LocalDate endDate);
    Tasks findTask(Integer idTask);

}

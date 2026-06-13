package com.gestion.system.service;

import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.model.entities.DaySubtasks;

import java.util.List;

public interface DaySubtaskService {

    DaySubtaskResponse getById(Integer idDaySubtask);
    List<DaySubtaskResponse> getByIdSubtask(Integer idSubtask);
    List<DaySubtaskResponse> getByIdDay(Integer idDay);
    DaySubtaskResponse create(DaySubtaskRequest daySubtaskRequest, Integer idUser);
    DaySubtaskResponse update(Integer idDaySubtask, DaySubtaskUpdateRequest updateRequest, Integer idUser);
    void delete(Integer idDaySubtask, Integer idUser);
    DaySubtasks findDaySubtask(Integer idDaySubtask);
}

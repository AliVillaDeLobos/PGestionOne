package com.gestion.system.service;

import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.request.update.DaysHoursUpdateRequest;
import com.gestion.system.dto.response.DaysHoursResponse;

import java.util.List;

public interface DaysHoursService {

    DaysHoursResponse create(DaysHoursRequest daysHoursRequest, Integer daySubtaskId, Integer userId);
    DaysHoursResponse getById(Integer idDay);
    List<DaysHoursResponse> getBySubtaskId(Integer subtaskId);
    DaysHoursResponse update(Integer updateId, DaysHoursUpdateRequest daysHoursRequest, Integer User);
    void delete(Integer idDay, Integer User);


}

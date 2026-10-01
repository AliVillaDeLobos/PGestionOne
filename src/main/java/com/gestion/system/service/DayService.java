package com.gestion.system.service;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.model.entities.Day;

import java.util.List;

public interface DayService {

    List<DayResponse> getDaysByWeek(Integer weekId);
    DayResponse getDayResponseById(Integer idDay);
    DayResponse getDayByNameAndWeek_Id(String nameDay, Integer weekId);
    Day findDay(Integer idDay);
}

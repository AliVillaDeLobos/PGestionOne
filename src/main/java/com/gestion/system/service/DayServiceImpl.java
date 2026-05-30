package com.gestion.system.service;

import com.gestion.system.dto.response.DayResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.response.DayMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.repositories.DaysRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DayServiceImpl implements DayService {
    private final DaysRepository daysRepository;
    private final DayMapper dayMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DayResponse> getDaysByWeek(Integer weekId) {
        List<Day> daysResponse = daysRepository.findByWeek_Id(weekId);
        if (daysResponse.isEmpty())
            throw new ResourceNotFoundException("Days with week's ID not found.");
        return dayMapper.listResponse(daysResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DayResponse getDayResponseById(Integer idDay) {
        Day day = daysRepository.findById(idDay).orElseThrow(
                () -> new ResourceNotFoundException("Day not found with this ID: " + idDay));
        return dayMapper.toResponse(day);
    }

    @Override
    @Transactional(readOnly = true)
    public DayResponse getDayByNameAndWeek_Id(String nameDay, Integer weekId) {
        Day day = daysRepository.findByDayNameAndWeek_Id(nameDay, weekId).orElseThrow(
                () -> new ResourceNotFoundException("Day not found with this week's ID and name."));
        return dayMapper.toResponse(day);
    }
}

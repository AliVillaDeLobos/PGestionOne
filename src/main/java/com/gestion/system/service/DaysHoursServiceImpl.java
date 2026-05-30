package com.gestion.system.service;

import com.gestion.system.dto.request.create.DaysHoursRequest;
import com.gestion.system.dto.request.update.DaysHoursUpdateRequest;
import com.gestion.system.dto.response.DaysHoursResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.DaysHoursMapper;
import com.gestion.system.mappers.update.DaysHoursUpdateMapper;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.DaysHours;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.repositories.DaysHoursRepository;
import com.gestion.system.repositories.DaysSubtasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DaysHoursServiceImpl implements DaysHoursService {
    private final DaysHoursRepository daysHoursRepository;
    private final DaysHoursMapper daysHoursMapper;
    private final DaysSubtasksRepository daysSubtasksRepository;
    private final AuditService auditService;
    private final DaysHoursUpdateMapper daysHoursUpdateMapper;

    private final UserAuthorizationService userAuthorization;


    @Override
    @Transactional
    public DaysHoursResponse create(DaysHoursRequest daysHoursRequest,Integer daySubtaskId , Integer userId) {
        DaySubtasks daySubtasks =  daysSubtasksRepository.findById(daySubtaskId).orElseThrow(
                () -> new ResourceNotFoundException("DaySubtask not found with ID: " + daySubtaskId));
        User user = userAuthorization.getAuthorizedUser(userId);
        DaysHours dh = daysHoursMapper.requestToEntity(daysHoursRequest, daySubtasks);
        DaysHours saved = daysHoursRepository.save(dh);

        DaysHoursResponse response = daysHoursMapper.toResponse(saved);
        auditService.create(AuditableEntity.DAYS_HOURS, response.getId(), user, response);
        return response;
    }

    @Override
    @Transactional
    public DaysHoursResponse update(Integer currentId, DaysHoursUpdateRequest requestDto, Integer userId) {
        DaysHours daysHours = daysHoursRepository.findById(currentId).orElseThrow(
                () -> new ResourceNotFoundException("DaysHours not found with ID: " + currentId));
        User user = userAuthorization.getAuthorizedUser(userId);

        DaysHours update = daysHoursUpdateMapper.updateEntity(requestDto, daysHours);
        DaysHours saved = daysHoursRepository.save(update);
        DaysHoursResponse response = daysHoursMapper.toResponse(saved);

        auditService.update(AuditableEntity.DAYS_HOURS, response.getId(), user
                ,daysHoursMapper.toResponse(daysHours), response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DaysHoursResponse getById(Integer idDayHours) {
        DaysHours daysHours = daysHoursRepository.findById(idDayHours).orElseThrow(
                () -> new ResourceNotFoundException("DaysHours not found with ID: " + idDayHours));
        return daysHoursMapper.toResponse(daysHours);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DaysHoursResponse> getBySubtaskId(Integer subtaskId) {
        List<DaysHours> listDh = daysHoursRepository.findAllByDaySubtask_Id(subtaskId);
        return daysHoursMapper.listResponse(listDh);
    }

    @Override
    @Transactional
    public void delete(Integer idDayHour, Integer userId) {
        DaysHours daysHours =  daysHoursRepository.findById(idDayHour).orElseThrow(
                () -> new ResourceNotFoundException("DayHour not found with ID: " + idDayHour));
        User user = userAuthorization.getAuthorizedUser(userId);

        auditService.delete(AuditableEntity.DAYS_HOURS, daysHours.getId(),
                user, daysHoursMapper.toResponse(daysHours));

        daysHoursRepository.delete(daysHours);
    }



}

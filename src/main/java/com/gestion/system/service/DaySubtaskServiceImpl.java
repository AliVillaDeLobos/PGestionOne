package com.gestion.system.service;

import com.gestion.system.dto.audit.DaySubtaskAuditModel;
import com.gestion.system.dto.request.create.DaySubtaskRequest;
import com.gestion.system.dto.request.update.DaySubtaskUpdateRequest;
import com.gestion.system.dto.response.DaySubtaskResponse;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.DaySubtaskMapper;
import com.gestion.system.mappers.update.DaySubtaskUpdateMapper;
import com.gestion.system.model.entities.Day;
import com.gestion.system.model.entities.DaySubtasks;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.DaysRepository;
import com.gestion.system.repositories.DaysSubtasksRepository;
import com.gestion.system.repositories.SubtaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DaySubtaskServiceImpl implements DaySubtaskService {
    private final DaysSubtasksRepository  daysSubtasksRepository;
    private final DaySubtaskMapper daySubtaskMapper;
    private final DaySubtaskUpdateMapper daySubtaskUpdateMapper;
    private final DaysRepository daysRepository;
    private final SubtaskRepository subtaskRepository;
    private final AuditService auditService;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional(readOnly = true)
    public DaySubtaskResponse getById(Integer idDaySubtask) {
        DaySubtasks daySubtasks =  daysSubtasksRepository.findById(idDaySubtask).orElseThrow(
                () -> new ResourceNotFoundException("DaySubtask not found with ID: " + idDaySubtask));
        return daySubtaskMapper.toResponse(daySubtasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DaySubtaskResponse> getByIdSubtask(Integer idSubtask) {
        List<DaySubtasks> list = daysSubtasksRepository.findAllBySubtask_Id(idSubtask);
        if (list.isEmpty()) throw new ResourceNotFoundException("DaySubtask not found with Subtask ID: " + idSubtask);
        return daySubtaskMapper.listResponse(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DaySubtaskResponse> getByIdDay(Integer idDay) {
        List<DaySubtasks> list = daysSubtasksRepository.findAllByDay_Id(idDay);
        if (list.isEmpty()) throw new ResourceNotFoundException("DaySubtask not found with Day ID: " + idDay);
        return daySubtaskMapper.listResponse(list);
    }


    @Override
    @Transactional
    public DaySubtaskResponse create(DaySubtaskRequest request, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Day day = daysRepository.findById(request.getIdDay()).orElseThrow(
                () -> new ResourceNotFoundException("Day not found with ID: " + request.getIdDay()));
        Subtask subtask = subtaskRepository.findById(request.getIdSubtask()).orElseThrow(
                () -> new ResourceNotFoundException("Subtask not found with ID: " + request.getIdSubtask()));
        DaySubtasks daySubtasks = daySubtaskMapper.requestToEntity(request, day, subtask);

        DaySubtasks saved = daysSubtasksRepository.save(daySubtasks);
        auditService.create(AuditableEntity.DAY_SUBTASK, saved.getId(), user, daySubtaskUpdateMapper.toAudit(saved));

        return daySubtaskMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DaySubtaskResponse update(Integer idDaySubtask, DaySubtaskUpdateRequest updateRequest, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        DaySubtasks daySubtasks = daysSubtasksRepository.findById(idDaySubtask).orElseThrow(
                () -> new ResourceNotFoundException("DaySubtask not found with ID: " + idDaySubtask));
        Day day =  daysRepository.findById(updateRequest.getIdDay()).orElseThrow(
                () -> new ResourceNotFoundException("Day not found with ID: " + updateRequest.getIdDay()));
        DaySubtaskAuditModel original = daySubtaskUpdateMapper.toAudit(daySubtasks);
        DaySubtasks updated = daySubtaskUpdateMapper.updateEntity(updateRequest, day, daySubtasks);
        auditService.update(AuditableEntity.DAY_SUBTASK,updated.getId(), user, original, daySubtaskUpdateMapper.toAudit(updated));

        return daySubtaskMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Integer idDaySubtask, Integer idUser) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        DaySubtasks daySubtasks = daysSubtasksRepository.findById(idDaySubtask).orElseThrow(
                () -> new ResourceNotFoundException("DaySubtask not found with ID: " + idDaySubtask));

        auditService.delete(AuditableEntity.DAY_SUBTASK, daySubtasks.getId(), user, daySubtaskUpdateMapper.toAudit(daySubtasks));
        daysSubtasksRepository.delete(daySubtasks);

    }
}

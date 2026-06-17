package com.gestion.system.service;

import com.gestion.system.dto.audit.SubtaskAuditModel;
import com.gestion.system.dto.request.create.SubtaskRequest;
import com.gestion.system.dto.request.update.SubtaskUpdateRequest;
import com.gestion.system.dto.response.SubtaskResponse;
import com.gestion.system.exceptions.InvalidResourceStateException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.SubtaskMapper;
import com.gestion.system.mappers.update.SubtaskUpdateMapper;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.Tasks;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.SubtaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SubtaskServiceImpl implements SubtaskService{
    private final SubtaskRepository subtaskRepository;
    private final TaskService taskService;
    private final SubtaskMapper subtaskMapper;
    private final SubtaskUpdateMapper subtaskUpdateMapper;
    private final SubtaskDeletedHistoryService subtaskDeletedHistory;
    private final AuditService audit;

    private final UserAuthorizationService userAuthorization;

    @Override
    @Transactional(readOnly = true)
    public SubtaskResponse getById(int idSubtask) {
        return subtaskMapper.toResponse(findSubtask(idSubtask));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubtaskResponse> getAllByTaskId(Integer idTask) {
        List<Subtask> subtasks = subtaskRepository.findAllByTask_IdAndIsDeletedFalse(idTask);
        return subtaskMapper.listResponse(subtasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubtaskResponse> getAllIsDeleted(Integer idTask) {
        List<Subtask> subtasks = subtaskRepository.findAllByTask_IdAndIsDeleted(idTask, true);
        return subtaskMapper.listResponse(subtasks);
    }


    @Transactional(readOnly = true)
    @Override
    public List<SubtaskResponse> getAllNotCompleted(Integer idTask) {
        List<Subtask> subtasks = subtaskRepository.findAllByTask_IdAndCompleted(idTask, false);
        return subtaskMapper.listResponse(subtasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubtaskResponse> getAllCompleted(Integer idTask) {
        List<Subtask> subtasks = subtaskRepository.findAllByTask_IdAndCompleted(idTask, true);
        return subtaskMapper.listResponse(subtasks);
    }

    @Override
    @Transactional
    public SubtaskResponse create(Integer idUser, SubtaskRequest request, Integer idTask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Tasks task = taskService.findTask(idTask);

        Subtask subtask = subtaskMapper.requestToEntity(request, task);
        subtaskRepository.save(subtask);
        SubtaskAuditModel original = subtaskUpdateMapper.toAudit(subtask);

        audit.create(AuditableEntity.SUBTASK,subtask.getId(),user,original);
        return subtaskMapper.toResponse(subtask);
    }

    @Override
    @Transactional
    public SubtaskResponse update(Integer idUser, Integer idSubtask, SubtaskUpdateRequest update, Integer idTask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MEMBER);
        Subtask subtask = subtaskRepository.findByIdAndTask_Id(idSubtask, idTask).orElseThrow(
                () -> new ResourceNotFoundException("Subtask not found in Task."));
        SubtaskAuditModel original = subtaskUpdateMapper.toAudit(subtask);

        subtaskUpdateMapper.updateEntity(update, subtask);
        subtaskRepository.save(subtask);

        audit.update(AuditableEntity.SUBTASK, subtask.getId(), user, original, subtaskUpdateMapper.toAudit(subtask));

        return subtaskMapper.toResponse(subtask);
    }

    @Override
    @Transactional
    public SubtaskResponse delete(Integer idUser, Integer idSubtask, String message, Integer idTask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Subtask subtask = subtaskRepository.findByIdAndTask_Id(idSubtask, idTask).orElseThrow(
                () -> new ResourceNotFoundException("Subtask not found in Task."));

        if (subtask.getIsDeleted()) throw new InvalidResourceStateException("Subtask already deleted.");

        subtask.setIsDeleted(true);
        subtaskRepository.save(subtask);

        subtaskDeletedHistory.registerDeletion(subtask, message);
        audit.delete(AuditableEntity.SUBTASK, subtask.getId(), user , subtaskUpdateMapper.toAudit(subtask));

        return subtaskMapper.toResponse(subtask);
    }

    @Override
    @Transactional
    public SubtaskResponse restore(Integer idUser, Integer idSubtask) {
        User user = userAuthorization.authorizeUser(idUser, SystemRole.MANAGER);
        Subtask subtask = findSubtask(idSubtask);

        if (!subtask.getIsDeleted()) throw new InvalidResourceStateException("Subtask is not deleted.");

        subtask.setIsDeleted(false);
        subtaskRepository.save(subtask);

        subtaskDeletedHistory.restore(idSubtask);
        audit.restore(AuditableEntity.SUBTASK, subtask.getId(), user, subtaskUpdateMapper.toAudit(subtask));

        return subtaskMapper.toResponse(subtask);
    }

    @Override
    @Transactional(readOnly = true)
    public Subtask findSubtask(Integer idSubtask) {
        return subtaskRepository.findById(idSubtask).orElseThrow(
                () -> new ResourceNotFoundException("Subtask not found with ID: " + idSubtask));
    }
}

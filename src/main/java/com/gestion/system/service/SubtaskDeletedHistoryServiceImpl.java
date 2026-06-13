package com.gestion.system.service;

import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.model.entities.Subtask;
import com.gestion.system.model.entities.SubtaskDeletedHistory;
import com.gestion.system.repositories.SubtaskDeletedHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class SubtaskDeletedHistoryServiceImpl implements SubtaskDeletedHistoryService {
    private final SubtaskDeletedHistoryRepository deletedHistoryRepository;
    private final SubtaskService subtaskService;

    @Override
    @Transactional
    public void registerDeletion(Subtask deletedSubtask, String message) {
        SubtaskDeletedHistory subtaskDeletedHistory = SubtaskDeletedHistory.builder()
                .subtask(deletedSubtask)
                .message(message)
                .deletedDate(LocalDate.now())
                .build();
        deletedHistoryRepository.save(subtaskDeletedHistory);
    }

    @Override
    @Transactional
    public void restore(Integer idSubtask) {
        SubtaskDeletedHistory deleted = deletedHistoryRepository.findBySubtask_Id(idSubtask).orElseThrow(
                () -> new ResourceNotFoundException("Subtask is no deleted with ID: " + idSubtask));
        deletedHistoryRepository.delete(deleted);
    }
}

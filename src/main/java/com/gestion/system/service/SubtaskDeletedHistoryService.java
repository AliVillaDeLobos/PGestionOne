package com.gestion.system.service;

import com.gestion.system.model.entities.Subtask;

public interface SubtaskDeletedHistoryService {

    void registerDeletion(Subtask deletedSubtask, String message);
    void restore(Integer idSubtask);
}

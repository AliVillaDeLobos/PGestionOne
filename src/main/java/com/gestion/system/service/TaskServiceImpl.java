package com.gestion.system.service;

import com.gestion.system.model.entities.Tasks;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Override
    public Tasks findTask(Integer idTask) {
        return null;
    }
}

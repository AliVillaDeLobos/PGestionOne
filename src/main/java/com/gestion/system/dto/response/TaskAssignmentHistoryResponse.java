package com.gestion.system.dto.response;

import com.gestion.system.model.enums.Action;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAssignmentHistoryResponse {

    private Integer id;
    private TaskResponse task;
    private UserResponse userAssigned;
    private UserResponse userAssignedBy;
    private Action action;
    private LocalDateTime actionDate;
}

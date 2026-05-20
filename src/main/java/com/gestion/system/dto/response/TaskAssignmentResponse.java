package com.gestion.system.dto.response;


import com.gestion.system.model.enums.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAssignmentResponse {

    private Integer id;
    private TaskResponse task;
    private UserResponse user;
    private Role role;
    private LocalDate assignedDate;
}

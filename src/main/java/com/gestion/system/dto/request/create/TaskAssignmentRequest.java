package com.gestion.system.dto.request.create;

import com.gestion.system.model.enums.ProjectRoles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAssignmentRequest {

     @NotNull(message = "Task ID is required for TaskAssignment.")
     @Positive
    private Integer taskId;
     @NotNull(message = "User ID is required for TaskAssignment.")
     @Positive
    private Integer userId;
     @NotNull(message = "Role ID is required for TaskAssignment.")
     @Enumerated(EnumType.STRING)
    private ProjectRoles projectRoles;
}

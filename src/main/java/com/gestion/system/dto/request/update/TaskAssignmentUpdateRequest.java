package com.gestion.system.dto.request.update;

import com.gestion.system.model.enums.ProjectRoles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAssignmentUpdateRequest {

     @Enumerated(EnumType.STRING)
    private ProjectRoles projectRoles;
}

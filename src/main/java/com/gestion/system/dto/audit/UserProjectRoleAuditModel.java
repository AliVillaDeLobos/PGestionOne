package com.gestion.system.dto.audit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectRoleAuditModel {
    private Integer idUserProject;
    private String role;
}

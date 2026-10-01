package com.gestion.system.dto.audit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectAuditModel {
    private Integer idUser;
    private Integer idProject;
    private String nameProject;
    private String emailUser;
}

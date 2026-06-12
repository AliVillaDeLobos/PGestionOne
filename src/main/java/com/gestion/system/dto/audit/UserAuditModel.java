package com.gestion.system.dto.audit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuditModel {
    private String name;
    private String paternalLastName;
    private String maternalLastName;
    private String email;
    private String role;
}

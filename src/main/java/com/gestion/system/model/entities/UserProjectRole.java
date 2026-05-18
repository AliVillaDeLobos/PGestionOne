package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_project_role")
public class UserProjectRole {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUserProjectRole;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user_project", nullable = false)
    private UserProject userProject;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_role", nullable = false)
    private Roles role;

}



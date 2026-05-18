package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Setter
@Table(name = "roles")
public class Roles {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRoles;

     @Column(name = "role_name", nullable = false)
    private String roleName;

}

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
     @Column(name = "id_role")
    private Integer id;

     @Column(name = "role_name", nullable = false)
    private String roleName;

}

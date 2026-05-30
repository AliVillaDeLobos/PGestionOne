package com.gestion.system.model.entities;

import com.gestion.system.model.enums.SystemRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_user")
    private Integer id;

     @Column(name = "user_password", nullable = false)
    private String password;

     @Column(name = "name",  nullable = false)
    private String name;

     @Column(name = "paternal_last_name", nullable = false)
    private String paternalLastName;

     @Column(name = "maternal_last_name", nullable = false)
    private String maternalLastName;

     @Column(name = "email", nullable = false)
    private String email;

     @Column(name = "id_system_role", nullable = false)
    private SystemRole systemRole;
}

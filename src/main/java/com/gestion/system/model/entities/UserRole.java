package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_role")
public class UserRole {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUserRole;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id_user", nullable = false)
     private User user;

      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id_role", nullable = false)
     private Roles role;
}

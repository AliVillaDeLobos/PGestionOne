package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_project")
public class UserProject {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUserProject;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user", nullable = false)
    private User user;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_project", nullable = false)
    private Project project;

}

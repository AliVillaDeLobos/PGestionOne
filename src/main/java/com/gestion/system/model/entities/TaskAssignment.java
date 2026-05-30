package com.gestion.system.model.entities;

import com.gestion.system.model.enums.ProjectRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "task_assignment")
public class TaskAssignment {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_task_assignment")
    private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_task", nullable = false)
    private Tasks task;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user", nullable = false)
    private User user;

     @Enumerated(EnumType.STRING)
     @Column (name = "assignment_type", nullable = false)
    private ProjectRoles projectRoles;

     @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

}

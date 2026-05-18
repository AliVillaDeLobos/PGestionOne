package com.gestion.system.model.entities;

import com.gestion.system.model.enums.Role;
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
    private Integer idTaskAssignment;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_task", nullable = false)
    private Tasks task;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user", nullable = false)
    private User user;

     @Enumerated(EnumType.STRING)
     @Column (name = "role", nullable = false)
    private Role role;

     @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

}

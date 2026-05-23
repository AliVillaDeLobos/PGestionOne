package com.gestion.system.model.entities;

import com.gestion.system.model.enums.Action;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "task_assignment_history")
public class TaskAssignmentHistory {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_task_assignment_history")
    private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_task", nullable = false)
    private Tasks task;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user_assigned", nullable = false)
    private User userAssigned;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_user_assigned_by", nullable = false)
    private User userAssignedBy;

     @Enumerated(EnumType.STRING)
     @Column(name = "action", nullable = false)
    private Action action;

     @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

}

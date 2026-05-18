package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "subtasks")
public class Subtask {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSubtask;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_task", nullable = false)
    private Tasks tasks;

     @Column (name = "description", nullable = false)
    private String description;

     @Column(name = "name", nullable = false)
    private String name;

     @Column(name = "status", nullable = false)
    private Boolean status;

     @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

     @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}

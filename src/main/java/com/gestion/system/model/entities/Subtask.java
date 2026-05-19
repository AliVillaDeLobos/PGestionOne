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
     @Column(name = "id_subtask")
    private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_task", nullable = false)
    private Tasks tasks;

     @Column (name = "description")
    private String description;

     @Column(name = "name", nullable = false)
    private String name;

     @Column(name = "completed", nullable = false)
    private Boolean completed;

     @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

     @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

}

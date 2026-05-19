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
@Table ( name = "subtask_deleted_history")
public class SubtaskDeletedHistory {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_subtask_deleted_history")
    private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_subtask", nullable = false)
    private Subtask subtask;

     @Column(name = "message", nullable = false)
    private String message;

     @Column(name = "deleted_date", nullable = false)
    private LocalDate daletedDate;
}

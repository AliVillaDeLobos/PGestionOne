package com.gestion.system.model.entities;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tasks")
public class Tasks {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_task")
    private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_project", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Colors color;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

     @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

     @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}

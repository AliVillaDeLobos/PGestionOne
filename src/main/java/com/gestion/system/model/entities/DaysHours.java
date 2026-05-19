package com.gestion.system.model.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "days_hours")
public class DaysHours {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_days_hours")
     private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_day_subtask")
    private DaysSubtasks daySubtask;

     @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;

     @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

}

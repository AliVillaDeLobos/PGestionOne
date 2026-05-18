package com.gestion.system.model.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer idDaysHours;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_day_subtask")
    private DaysSubtasks daySubtask;

     @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

     @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

}

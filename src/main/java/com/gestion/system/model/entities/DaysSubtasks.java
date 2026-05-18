package com.gestion.system.model.entities;

import com.gestion.system.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table( name = "days_subtasks")
public class DaysSubtasks {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDaySubtask;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_subtask", nullable = true) //Por defecto esta en PENDING EN DB
    private Subtask subtask;

     @Column(name = "status", nullable = false)
    private Status status;


      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id_day", nullable = false)
     private Day day;


}

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
     @Column(name = "id_day_subtask")
     private Integer id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "id_subtask", nullable = false)
    private Subtask subtask;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") //Por defecto esta en PENDING EN DB
    private Status status;


      @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "id_day", nullable = false)
     private Day day;


}

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
@Table(name = "project")
public class Project {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id_project")
    private Integer id;

     @Column(name = "name", nullable = false)
    private String name;

     @Column(name = "description", nullable = false)
    private String description;

     @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

}

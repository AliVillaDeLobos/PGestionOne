package com.gestion.system.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Immutable
@Table(name = "weeks")
public class Week {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idWeek;

     @Column( name = "week_num", updatable = false)
    private Integer weekNum;

     @Column(name = "year",  updatable = false)
    private Integer year;

     @Column(name = "start_date", updatable = false)
    private LocalDate startDate;

     @Column(name = "end_date", updatable = false)
    private LocalDate endDate;

     // Se usara para mappear las semanas y dias del calendario si es necesario
     @OneToMany(mappedBy = "week", fetch = FetchType.LAZY)
     @ToString.Exclude
     @JsonIgnore
    private List<Day> days;
}

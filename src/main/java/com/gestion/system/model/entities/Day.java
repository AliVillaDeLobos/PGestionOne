package com.gestion.system.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Immutable
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_day")
    private Integer idDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_week", nullable = false)
    private Week week;

     @Column(name = "date", nullable = false)
    private LocalDate date;

     @Column(name = "day_name", nullable = false)
    private String dayName;

}

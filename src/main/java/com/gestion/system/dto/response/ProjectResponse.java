package com.gestion.system.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {

    private Integer id;
    private String name;
    private String description;
    private LocalDate startDate;

}

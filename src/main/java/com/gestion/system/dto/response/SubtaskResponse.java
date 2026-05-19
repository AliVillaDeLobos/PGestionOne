package com.gestion.system.dto.response;


import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtaskResponse {

    private Integer id;

    private String description;
    private String name;
    private Boolean status;
    private LocalDate startDate;
    private Boolean isDeleted;
    private TaskResponse task;

}

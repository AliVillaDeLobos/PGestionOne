package com.gestion.system.dto.response;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Integer id;
//    private PorjectResponse project;
    private Colors color;
    private String name;
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;

}

package com.gestion.system.dto.request.filters;

import com.gestion.system.model.enums.Colors;
import com.gestion.system.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TaskFilterRequest {

    private String name;
    private Colors color;
    private Status status;


}

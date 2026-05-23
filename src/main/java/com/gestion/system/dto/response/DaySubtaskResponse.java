package com.gestion.system.dto.response;

import com.gestion.system.model.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DaySubtaskResponse {

    private Integer id;
    private SubtaskResponse subtask;
    private Status status;
    private DayResponse day;

}


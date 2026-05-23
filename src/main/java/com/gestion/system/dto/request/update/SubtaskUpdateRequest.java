package com.gestion.system.dto.request.update;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskUpdateRequest {

    private String description;
    private String name;
    private Boolean completed;

}

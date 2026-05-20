package com.gestion.system.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskUpdateRequest {

    private String description;
    private String name;

}

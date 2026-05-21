package com.gestion.system.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProjectResponse {

    private Integer id;
    private UserResponse user;
    private ProjectResponse project;

}

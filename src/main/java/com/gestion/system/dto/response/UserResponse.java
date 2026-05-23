package com.gestion.system.dto.response;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {


    private Integer id;
    private String name;
    private String lastNames;
    private String email;

}

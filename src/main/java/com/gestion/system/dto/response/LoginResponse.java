package com.gestion.system.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {
    String message;
    String name;
}

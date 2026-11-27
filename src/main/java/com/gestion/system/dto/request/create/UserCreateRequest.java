package com.gestion.system.dto.request.create;

import com.gestion.system.model.enums.SystemRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {

     @Valid
     @NotNull
    private UserRequest userRequest;

     @NotNull
    private SystemRole role;
}

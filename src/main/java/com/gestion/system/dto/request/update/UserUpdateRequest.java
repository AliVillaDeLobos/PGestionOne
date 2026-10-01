package com.gestion.system.dto.request.update;

import com.gestion.system.validations.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String name;

     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String firstLastName;

     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String secondLastName;


}

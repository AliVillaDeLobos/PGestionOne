package com.gestion.system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private static final String NAME_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String name;

     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String firstLastName;

     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String secondLastName;

     @Email(message = "El email no es valido.")
     @Size(max = 50)
    private String email;

}

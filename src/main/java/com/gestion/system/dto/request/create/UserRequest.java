package com.gestion.system.dto.request.create;

import com.gestion.system.validations.ValidationPatterns;
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
public class UserRequest {



     @NotBlank(message = "El nombre del usuario es obligatorio.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String name;

     @NotBlank(message = "El primer apellido del usuario es obligarotio.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String firstLastName;

     @NotBlank(message = "El segundo apellido del usuario es obligatorio.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String secondLastName;

     @NotBlank(message = "El email del usuario es obligatorio.")
     @Email(message = "El email no es valido.")
     @Size(max = 50, message = "Max 50 characters.")
    private String email;

}

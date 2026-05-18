package com.gestion.system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    //Si se expande mover a public final class ValidationPatterns
    private static final String NAME_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

     @NotBlank
     @Pattern(
             regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

     @NotBlank(message = "El nombre del usuario es obligatorio.")
     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String name;

     @NotBlank(message = "El primer apellido del usuario es obligarotio.")
     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String firstLastName;

     @NotBlank(message = "El segundo apellido del usuario es obligatorio.")
     @Pattern(regexp = NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25)
    private String secondLastName;

     @NotBlank(message = "El email del usuario es obligatorio.")
     @Email(message = "El email no es valido.")
     @Size(max = 50)
    private String email;

}

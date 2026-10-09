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

     @NotBlank(message = "Name is required.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "The name only content letters.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String name;

     @NotBlank(message = "Last name cannot be null.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String firstLastName;

     @NotBlank(message = "Second last name cannot be null.")
     @Pattern(regexp = ValidationPatterns.NAME_REGEX, message = "Solo se permiten letras.")
     @Size(min = 2, max = 25, message = "Min 2 and max 25 letters.")
    private String secondLastName;

     @NotBlank(message = "User email cannot be null.")
     @Email(message = "The mail is not valid.")
     @Size(max = 50, message = "Max 50 characters.")
    private String email;

}

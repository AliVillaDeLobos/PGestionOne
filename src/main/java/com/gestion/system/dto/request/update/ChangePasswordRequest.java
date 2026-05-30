package com.gestion.system.dto.request.update;

import com.gestion.system.validations.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {

     @NotBlank
     @Pattern(regexp = ValidationPatterns.PWD_REGEX,
             message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String currentPassword;

     @NotBlank
     @Pattern(regexp = ValidationPatterns.PWD_REGEX,
            message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial")
    private String newPassword;
}

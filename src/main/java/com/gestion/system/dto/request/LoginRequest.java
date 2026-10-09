package com.gestion.system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

     @NotBlank(message = "User email cannot be null.")
     @Email(message = "The email is not valid.")
     @Size(max = 50, message = "Max 50 characters.")
    private String email;

     @NotBlank(message = "The password cannot be null.")
     @Size(max = 50, message = "Max 50 characters.")
    private String password;

}

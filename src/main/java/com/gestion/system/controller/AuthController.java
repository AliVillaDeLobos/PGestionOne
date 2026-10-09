package com.gestion.system.controller;

import com.gestion.system.dto.request.LoginRequest;
import com.gestion.system.dto.response.LoginResponse;
import com.gestion.system.service.LoginService;
import com.gestion.system.service.UserAuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pgestion_one/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;

     @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
     }

}

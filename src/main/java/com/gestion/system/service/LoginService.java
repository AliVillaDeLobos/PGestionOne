package com.gestion.system.service;

import com.gestion.system.dto.request.LoginRequest;
import com.gestion.system.dto.response.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public interface LoginService {

    LoginResponse login(LoginRequest request);
    void logout(String token);

}

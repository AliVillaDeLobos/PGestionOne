package com.gestion.system.service;

import com.gestion.system.dto.request.LoginRequest;
import com.gestion.system.dto.response.LoginResponse;
import com.gestion.system.exceptions.PasswordInvalidateException;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.model.entities.User;
import com.gestion.system.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserAuthorizationService userAuthorization;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userAuthorization.findUserByEmail(request.getEmail());
        validPassword(request.getPassword(),  user);
        return LoginResponse.builder().message("Welcome back ").name(user.getName()).build();
    }

    @Override
    public void logout(String token) {

    }

    private void validPassword(String password, User user) {
        if (password == null || password.isBlank()) {
            throw new PasswordInvalidateException("Your password cannot be empty");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordInvalidateException("Your password is incorrect.");
        }
    }
}

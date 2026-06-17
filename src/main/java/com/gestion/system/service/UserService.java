package com.gestion.system.service;

import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.request.update.ChangePasswordRequest;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;

public interface UserService {
    UserResponse  create(UserRequest userRequest, Integer createUserId, SystemRole role);
    UserResponse update(Integer userId, UserUpdateRequest userUpdate);
    void updatePassword(Integer userId, ChangePasswordRequest changePasswordRequest);
    UserResponse getById(Integer userId);
    UserResponse getByEmail(String email);
    void delete(Integer userId, Integer adminId);
    User findUser(Integer idUser);

}

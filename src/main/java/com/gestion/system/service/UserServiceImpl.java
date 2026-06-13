package com.gestion.system.service;

import com.gestion.system.dto.request.create.UserRequest;
import com.gestion.system.dto.request.update.ChangePasswordRequest;
import com.gestion.system.dto.request.update.UserUpdateRequest;
import com.gestion.system.dto.response.UserResponse;
import com.gestion.system.exceptions.PasswordInvalidateException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.mappers.request.UserMapper;
import com.gestion.system.mappers.update.UserUpdateMapper;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.AuditableEntity;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthorizationService userAuthorization;
    private final AuditService auditService;

    @Override
    @Transactional
    public UserResponse create(UserRequest userRequest, Integer createUserId) {
        User userCreate = userAuthorization.authorizeUser(createUserId, SystemRole.ROOT);
        User user = userMapper.requestToEntity(userRequest);

        user = usersRepository.save(user);
        UserResponse response = userMapper.toResponse(user);

        auditService.create(AuditableEntity.USER, user.getId(), userCreate, userUpdateMapper.toAudit(user));
        return response;
    }

    @Override
    @Transactional
    public UserResponse update(Integer userId, UserUpdateRequest userUpdate) {
        User user = findUser(userId);
        user = userUpdateMapper.updateEntity(userUpdate, user);
        usersRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Integer userId)  {
        User user = findUser(userId);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User user = usersRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, ChangePasswordRequest changePasswordRequest) {
        User user = findUser(userId);
        passwordValidator(changePasswordRequest, user);
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Integer targetId, Integer actingId) {
        User requester = findUser(actingId);
        User target = findUser(targetId);

        userAuthorization.validateDeleteUserPermission(target, requester);

        auditService.delete(AuditableEntity.USER, target.getId(), requester, userUpdateMapper.toAudit(target));

        usersRepository.delete(target);
    }


    private void passwordValidator (ChangePasswordRequest request, User user) {
        if( request.getCurrentPassword().isBlank() || request.getNewPassword().isBlank())
            throw new PasswordInvalidateException("You must enter all the required fields");
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new PasswordInvalidateException("Current password is incorrect.");
        if (request.getCurrentPassword().equals(request.getNewPassword()))
            throw new PasswordInvalidateException("New password and old password cannot be the same.");
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Integer idUser){
        return usersRepository.findById(idUser).orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + idUser));
    }
}

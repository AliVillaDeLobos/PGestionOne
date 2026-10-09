package com.gestion.system.service;

import com.gestion.system.exceptions.InvalidDeletionUserException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.exceptions.UnauthorizedOperationException;
import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;
import com.gestion.system.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {
    private final UsersRepository usersRepository;

     @Override
    public void authorize(User requester, SystemRole requiredRole) {
        if(!requester.getSystemRole().hasAuthority(requiredRole))
            throw new UnauthorizedOperationException("You are not authorized to perform this operation.");
    }

     @Override
     @Transactional(readOnly = true)
    public User authorizeUser(Integer userId, SystemRole requiredRole){
        User user = usersRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID: " + userId));
        authorize(user, requiredRole);
        return user;
    }

    @Override
    public void validateDeleteUserPermission(User targetUser, User actingUser) {
        Integer targetRole = targetUser.getSystemRole().getAuthorityLevel();
        Integer requesterRole = actingUser.getSystemRole().getAuthorityLevel();
        authorize(actingUser, SystemRole.ADMIN);
        if (!actingUser.getSystemRole().canDeleteUsers() || requesterRole <= targetRole) {
                throw new InvalidDeletionUserException(
                    "You are not allowed to delete user with equal or higher privileges.");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email));
    }
}

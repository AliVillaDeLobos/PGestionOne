package com.gestion.system.service;

import com.gestion.system.exceptions.InvalidDeletionUserException;
import com.gestion.system.exceptions.ResourceNotFoundException;
import com.gestion.system.exceptions.UnauthorizedOperationException;
import com.gestion.system.model.entities.User;
import com.gestion.system.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {
    private final UsersRepository usersRepository;


    @Override
    public void validateDeleteUserPermission(User targetUser, User actingUser) {
        Integer targetRole = targetUser.getSystemRole().getAuthorityLevel();
        Integer requesterRole = actingUser.getSystemRole().getAuthorityLevel();

        if (!actingUser.getSystemRole().canDeleteUsers() || requesterRole <= targetRole) {
            throw new InvalidDeletionUserException(
                    "You are not allowed to delete this user");
        }
    }

    @Override
    public void validateEntityPermission(User requester) {
        if(!requester.getSystemRole().canModifyEntities())
            throw new UnauthorizedOperationException("You are not allowed to delete this field.");
    }


    public User getAuthorizedUser(Integer userId){
        User user = usersRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with ID: " + userId));
        validateEntityPermission(user);
        return user;
    }

}

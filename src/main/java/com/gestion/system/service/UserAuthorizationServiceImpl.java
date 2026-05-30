package com.gestion.system.service;

import com.gestion.system.exceptions.InvalidDeletionUserException;
import com.gestion.system.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthorizationServiceImpl implements UserAuthorizationService {


    @Override
    public void validateDeletePermission(User targetUser, User actingUser) {
        Integer targetRole = targetUser.getSystemRole().getLevel();
        Integer requesterRole = actingUser.getSystemRole().getLevel();

        if (requesterRole < 4 || requesterRole <= targetRole) {
            throw new InvalidDeletionUserException(
                    "You are not allowed to delete this user");
        }
    }
}

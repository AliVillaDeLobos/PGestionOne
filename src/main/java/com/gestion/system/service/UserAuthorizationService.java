package com.gestion.system.service;

import com.gestion.system.model.entities.User;
import com.gestion.system.model.enums.SystemRole;

public interface UserAuthorizationService {

    void validateDeleteUserPermission(User target, User requester);
    void authorize(User requester, SystemRole requiredRole);
    User authorizeUser(Integer userId, SystemRole requiredRole);

}

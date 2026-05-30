package com.gestion.system.service;

import com.gestion.system.model.entities.User;

public interface UserAuthorizationService {

    void validateDeleteUserPermission(User target, User requester);
    void validateEntityPermission(User requester);
    User getAuthorizedUser(Integer userId);

}

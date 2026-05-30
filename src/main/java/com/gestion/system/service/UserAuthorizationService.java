package com.gestion.system.service;

import com.gestion.system.model.entities.User;

public interface UserAuthorizationService {
    void  validateDeletePermission(User target, User requester);
}

package com.gestion.system.model.enums;


import lombok.Getter;

@Getter
public enum SystemRole {
    ROOT(1, 5),
    ADMIN(3, 4),
    MANAGER(2, 3),
    MEMBER(4, 2);

    private final Integer code;
    private final Integer authorityLevel;

    SystemRole(Integer code, Integer authorityLevel) {
        this.code = code;
        this.authorityLevel = authorityLevel;
    }

    public boolean canModifyEntities(){
        return authorityLevel >= MANAGER.getAuthorityLevel();
    }

    public boolean hasAuthority(SystemRole requiredRole){
        return this.authorityLevel >= requiredRole.getAuthorityLevel();
    }

    public boolean canDeleteUsers() {
        return authorityLevel >= ADMIN.getAuthorityLevel();
    }

    public boolean canDeleteAdmins() {
        return authorityLevel >= ADMIN.getAuthorityLevel();
    }
}

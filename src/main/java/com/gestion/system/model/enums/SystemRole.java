package com.gestion.system.model.enums;

public enum SystemRole {
    ROOT(1, 5),
    MANAGER(2, 3),
    ADMIN(3, 4),
    MEMBER(4, 2),
    VIEWER(5, 1);

    private final Integer code;
    private final Integer level;

    SystemRole(Integer code, Integer level) {
        this.code = code;
        this.level = level;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getLevel() {
        return level;
    }
}

package com.gestion.system.exceptions;

public class PasswordInvalidateException extends RuntimeException {
    public PasswordInvalidateException(String message) {
        super(message);
    }
}

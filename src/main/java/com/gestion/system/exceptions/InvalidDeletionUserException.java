package com.gestion.system.exceptions;

public class InvalidDeletionUserException extends RuntimeException {
    public InvalidDeletionUserException(String message) {
        super(message);
    }
}

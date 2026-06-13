package com.gestion.system.exceptions;

public class InvalidResourceStateException extends RuntimeException {
    public InvalidResourceStateException(String message) {
        super(message);
    }
}

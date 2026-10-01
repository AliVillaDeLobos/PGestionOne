package com.gestion.system.exceptions.BusinessRulesExceptions;

public class DuplicateAssignmentException extends BusinessRuleException {
    public DuplicateAssignmentException() {
        super("User is already assigned to this project");
    }
}

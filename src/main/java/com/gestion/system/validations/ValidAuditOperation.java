package com.gestion.system.validations;

import com.gestion.system.model.entities.Audit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAuditOperation implements ConstraintValidator<ValidAudit, Audit> {


    @Override
    public void initialize(ValidAudit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Audit audit, ConstraintValidatorContext context) {
        if (audit == null || audit.getOperation() == null) {
            return false;
        }
             return switch (audit.getOperation()) {

                case CREATE -> fail(context,
                        audit.getOldData() != null || audit.getNewData() == null,
                        "CREATE: NewData cannot be null and OldData should be null");

                case UPDATE -> fail(context,
                        audit.getOldData() == null || audit.getNewData() == null,
                        "UPDATE: NewData and OldData cannot be null");

                case DELETE -> fail(context,
                        audit.getOldData() == null || audit.getNewData() != null,
                        "DELETE: OldData cannot be null and NewData should be null");
                case RESTORE -> fail(context,
                                audit.getOldData() == null || audit.getNewData() == null,
                                "RESTORE: New Data ando Old Data cannot be null");
            };

    }

    private boolean fail(
            ConstraintValidatorContext context,
            boolean hasFailed,
            String message
    ) {
        if (hasFailed) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }


}

package com.gestion.system.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;


    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.start();
        this.endField = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field start = obj.getClass().getDeclaredField(startField);

            Field end = obj.getClass().getDeclaredField(endField);

            start.setAccessible(true);
            end.setAccessible(true);

            LocalDateTime startDate = (LocalDateTime) start.get(obj);
            LocalDateTime endDate = (LocalDateTime) end.get(obj);

            if ( startDate == null || endDate == null){
                return true; //Se deja tru porque la regla de null se evalua con @NotNull, no aquí
            }

            return endDate.isBefore(startDate);

        }catch (Exception e) {
            return false;
        }

    }
}

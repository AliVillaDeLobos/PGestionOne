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

            //Se usa Object para comparar de forma general los campos Date
            Object startValue =  start.get(obj);
            Object endValue =  end.get(obj);

            if ( startValue == null || endValue == null)
                return true; //Se deja tru porque la regla de null se evalua con @NotNull, no aquí

            if (!startValue.getClass().equals(endValue.getClass()))
                return false;

            if (!(startValue instanceof Comparable))
                    return false;

            Comparable startComp = (Comparable) startValue;
            Comparable endComp = (Comparable) endValue;

            return endComp.compareTo(startComp) >= 0;

        }catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }

    }
}

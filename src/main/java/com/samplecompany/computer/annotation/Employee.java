package com.samplecompany.computer.annotation;

import com.samplecompany.computer.annotation.validator.EmployeeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmployeeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Employee {
  String message() default "Invalid employeeAbbreviation: must be only 3 chars";
  public Class<?>[] groups() default {};
  public Class<? extends Payload>[] payload() default {};
}

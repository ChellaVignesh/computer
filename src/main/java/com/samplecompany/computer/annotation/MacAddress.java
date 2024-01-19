package com.samplecompany.computer.annotation;

import com.samplecompany.computer.annotation.validator.MacAddressFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MacAddressFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MacAddress {
  public String message() default "Invalid mac Address: should follow XX:XX:XX:XX:XX:XX pattern";
  public Class<?>[] groups() default {};
  public Class<? extends Payload>[] payload() default {};
}
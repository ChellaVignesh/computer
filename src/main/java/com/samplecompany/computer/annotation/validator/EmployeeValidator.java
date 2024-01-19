package com.samplecompany.computer.annotation.validator;

import com.samplecompany.computer.annotation.Employee;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmployeeValidator implements ConstraintValidator<Employee, String> {

  @Override
  public void initialize(Employee constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return false; // null values are considered invalid
    }
    return value.length() == 3;
  }
}

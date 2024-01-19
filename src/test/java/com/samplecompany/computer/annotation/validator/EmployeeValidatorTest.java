package com.samplecompany.computer.annotation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EmployeeValidatorTest {

  @InjectMocks
  private EmployeeValidator validator;

  private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  @Test
  void testValidEmployeeAbbreviation() {
    assertTrue(validator.isValid("abc", context));
    assertTrue(validator.isValid("123", context));
    assertTrue(validator.isValid("XYZ", context));
  }

  @Test
  void testInvalidEmployeeAbbreviation() {
    assertFalse(validator.isValid(null, context));
    assertFalse(validator.isValid("", context));
    assertFalse(validator.isValid("ab", context));
    assertFalse(validator.isValid("abcd", context));
  }
}


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
class MacAddressFormatValidatorTest {

  @InjectMocks
  private MacAddressFormatValidator validator;

  private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  @Test
  void testValidMacAddress() {
    assertTrue(validator.isValid("00:1A:2B:3C:4D:5E", context));
    assertTrue(validator.isValid("00-1A-2B-3C-4D-5E", context));
  }

  @Test
  void testInvalidMacAddress() {
    assertFalse(validator.isValid(null, context));
    assertFalse(validator.isValid("", context));
    assertFalse(validator.isValid("invalid_mac_address", context));
    assertFalse(validator.isValid("00:1A:2B:3C:4D:5EG", context));
    assertFalse(validator.isValid("00:1A:2B:3C:4D:5", context));
  }
}


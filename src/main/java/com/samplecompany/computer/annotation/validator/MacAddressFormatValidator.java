package com.samplecompany.computer.annotation.validator;

import com.samplecompany.computer.annotation.MacAddress;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MacAddressFormatValidator implements ConstraintValidator<MacAddress, String> {
  private static final String MAC_ADDRESS_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

  @Override
  public void initialize(MacAddress constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(value)) {
      return false; // null values are considered invalid
    }

    return Pattern.matches(MAC_ADDRESS_PATTERN, value);
  }
}

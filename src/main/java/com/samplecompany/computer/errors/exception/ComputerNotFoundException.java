package com.samplecompany.computer.errors.exception;

public class ComputerNotFoundException extends RuntimeException {
  public ComputerNotFoundException() {
    super();
  }

  public ComputerNotFoundException(String macAddress) {
    super(String.format("Computer Not found with MacAddress: {}", macAddress));
  }
}

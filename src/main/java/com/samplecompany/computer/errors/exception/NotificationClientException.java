package com.samplecompany.computer.errors.exception;

public class NotificationClientException extends RuntimeException {
  public NotificationClientException() {
    super();
  }

  public NotificationClientException(String macAddress) {
    super("Notification Client Call Failed");
  }
}

package com.samplecompany.computer.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Error implements Serializable {
  COMPUTER_NOT_FOUND(HttpStatus.NOT_FOUND, "Computer not found"),
  NOTIFICATION_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Notification Client Call Failed"),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Request is Invalid");

  private final HttpStatus code;

  private final String message;

  Error(HttpStatus code, String message) {
    this.code = code;
    this.message = message;
  }

  public HttpStatus getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}

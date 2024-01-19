package com.samplecompany.computer.errors;

import static com.samplecompany.computer.errors.Error.COMPUTER_NOT_FOUND;
import static com.samplecompany.computer.errors.Error.NOTIFICATION_CALL_FAILED;

import com.samplecompany.computer.errors.exception.ComputerNotFoundException;
import com.samplecompany.computer.errors.exception.NotificationClientException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = ComputerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Error> handleUserNotFoundException(final ComputerNotFoundException e) {
    return new ResponseEntity<>(COMPUTER_NOT_FOUND, COMPUTER_NOT_FOUND.getCode());
  }

  @ExceptionHandler(value = NotificationClientException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Error> handleNotificationClientException(final NotificationClientException e) {
    return new ResponseEntity<>(NOTIFICATION_CALL_FAILED, NOTIFICATION_CALL_FAILED.getCode());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      if (error instanceof FieldError fieldError) {
        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
      } else {
        errors.put(error.getObjectName(), error.getDefaultMessage());
      }
    });

    return ResponseEntity.badRequest().body(errors);
  }
}

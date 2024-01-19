package com.samplecompany.computer.errors;

import com.samplecompany.computer.errors.exception.ComputerNotFoundException;
import com.samplecompany.computer.errors.exception.NotificationClientException;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  void handleUserNotFoundException() {
    ComputerNotFoundException computerNotFoundException = new ComputerNotFoundException("Computer not found");
    ResponseEntity<Error> responseEntity = exceptionHandler.handleUserNotFoundException(computerNotFoundException);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertEquals("Computer not found", Objects.requireNonNull(responseEntity.getBody()).getMessage());
  }

  @Test
  void handleNotificationClientException() {
    NotificationClientException notificationClientException = new NotificationClientException("Notification Client Call Failed");
    ResponseEntity<Error> responseEntity = exceptionHandler.handleNotificationClientException(notificationClientException);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    assertEquals("Notification Client Call Failed", Objects.requireNonNull(responseEntity.getBody()).getMessage());
  }

  @Test
  void handleValidationException() {
    MethodArgumentNotValidException exception = createValidationException();

    ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleValidationException(exception);

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertTrue(Objects.requireNonNull(responseEntity.getBody()).containsKey("macAddress"));
    assertTrue(responseEntity.getBody().containsValue("Mac Address cannot be blank"));
  }

  private MethodArgumentNotValidException createValidationException() {
    // You need to create a BindingResult along with FieldError objects.
    // Here's an example with a single field error for demonstration purposes.
    BindingResult bindingResult = new BindException(new Object(), "target");
    bindingResult.addError(new FieldError("target", "macAddress", "Mac Address cannot be blank"));

    return new MethodArgumentNotValidException(null, bindingResult);
  }
}


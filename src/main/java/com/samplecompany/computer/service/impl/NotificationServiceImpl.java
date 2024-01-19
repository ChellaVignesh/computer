package com.samplecompany.computer.service.impl;

import com.samplecompany.computer.errors.exception.NotificationClientException;
import com.samplecompany.computer.model.Notification;
import com.samplecompany.computer.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class NotificationServiceImpl implements NotificationService {
  private final WebClient webClient;

  private static final String NOTIFY_URI = "/api/notify";
  private static final String WARNING = "warning";

  public NotificationServiceImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public void sendNotification(String employeeAbbreviation) {
    String notificationMessage = "Employee " + employeeAbbreviation + " has 3 or more assigned computers.";

    webClient.post()
        .uri(NOTIFY_URI)
        .body(BodyInserters.fromValue(
            new Notification(WARNING, employeeAbbreviation, notificationMessage)))
        .retrieve()
        .toBodilessEntity()
        .retry(2)
        .onErrorResume(WebClientResponseException.class,
            throwable -> Mono.error(NotificationClientException::new))
        .block();
  }
}

package com.samplecompany.computer.service.impl;

import com.samplecompany.computer.errors.exception.NotificationClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class NotificationServiceImplTest {

  @Mock
  private WebClient webClient;

  private WebClient.RequestBodySpec requestBodyMock;

  private WebClient.RequestHeadersSpec requestHeadersSpecMock;

  private WebClient.RequestBodyUriSpec requestHeadersUriSpecMock;

  private WebClient.ResponseSpec responseSpecMock;
  @InjectMocks
  private NotificationServiceImpl notificationService;

  @BeforeEach
  void mockWebClient() {
    requestHeadersUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
    requestBodyMock = mock(WebClient.RequestBodySpec.class);

    requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
    responseSpecMock = mock(WebClient.ResponseSpec.class);

    when(webClient.post()).thenReturn(requestHeadersUriSpecMock);
    when(requestHeadersUriSpecMock.uri("/api/notify")).thenReturn(requestBodyMock);
    when(requestBodyMock.body(any())).thenReturn(requestHeadersSpecMock);
    when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
  }

  @Test
  void testSendNotification() {
    // Mocking WebClient behavior
    when(responseSpecMock.toBodilessEntity()).thenReturn(Mono.empty());

    // Executing the method under test
    notificationService.sendNotification("emp1");

    // Verifying that the WebClient was called with the correct parameters
    verify(webClient, times(1)).post();
    verify(requestHeadersUriSpecMock, times(1)).uri("/api/notify");
    verify(requestBodyMock, times(1)).body(any());
    verify(requestHeadersSpecMock, times(1)).retrieve();
    verify(responseSpecMock, times(1)).toBodilessEntity();
  }

  @Test
  void testSendNotificationWebClientError() {
    when(responseSpecMock.toBodilessEntity()).thenReturn(Mono.error(new WebClientResponseException(404, "Not Found", null, null, null)));
    assertThrows(NotificationClientException.class, () -> notificationService.sendNotification("emp1"));
  }
}


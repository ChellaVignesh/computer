package com.samplecompany.computer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NotificationClientConfig {

  @Bean
  public WebClient webClient(
      @Value("${notification.service.endpoint:http://localhost:8080/}") String baseUrl,
      WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(baseUrl).build();
  }
}

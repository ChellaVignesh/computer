package com.samplecompany.computer.controller;

import com.samplecompany.computer.model.Computer;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdministratorControllerIntegrationTest {
  private static final Logger logger = LoggerFactory.getLogger(AdministratorControllerIntegrationTest.class);
  @LocalServerPort
  private int port;
  WebClient webClient;

  @BeforeEach
  public void setUp() {
    webClient = WebClient.builder().baseUrl("http://localhost:".concat(String.valueOf(port))).build();
  }

  @Container
  private static final GenericContainer<?> greenboneAdminNotificationService = new GenericContainer<>("greenbone/exercise-admin-notification:latest")
      .withExposedPorts(8080)
      .withCommand("-p", "8080:8080")
      .withLogConsumer(new Slf4jLogConsumer(logger)) // Redirect logs to SLF4J
      .withStartupTimeout(Duration.ofSeconds(5)); // Wait until the service is ready

  // DynamicPropertySource to set the base URL for the service
  @DynamicPropertySource
  static void setServiceProperties(DynamicPropertyRegistry registry) {
    registry.add("notification.service.endpoint", () ->
        "http://" + greenboneAdminNotificationService.getHost() + ":" + greenboneAdminNotificationService.getMappedPort(8080));
  }

  @Test
  void test_notifyAdminServiceOnAdding3Computers() {
    createComputer("01:1A:2B:3C:4D:5E");
    createComputer("02:1A:2B:3C:4D:5E");
    createComputer("03:1A:2B:3C:4D:5E");

    String logs = greenboneAdminNotificationService.getLogs();

    // Assert the logs of the docker container
    assertThat(logs).contains("Notification successfully received: {Level:warning EmployeeAbbreviation:123 Message:Employee 123 has 3 or more assigned computers");
  }

  @Test
  void test_createComputer() {
    // GIVEN
    Computer computer = new Computer("02:1A:2B:3C:4D:5E", "Computer123", "192.168.1.1", "123", "Description");
    Mono<Computer> postResult = webClient.post().uri("/api/computer").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(computer)).retrieve().bodyToMono(Computer.class);

    // THEN
    StepVerifier.create(postResult)
        .expectNext(computer)
        .verifyComplete();
  }

  @Test
  void test_getAllComputers() {
    Computer computer = createComputer("02:1A:2B:3C:4D:5E");

    ParameterizedTypeReference<List<Computer>> computerListType = new ParameterizedTypeReference<List<Computer>>() {};
    Mono<List<Computer>> getResult = webClient.get().uri("/api/computer")
        .retrieve().bodyToMono(computerListType);

    // THEN
    StepVerifier.create(getResult)
        .assertNext(computers -> Assertions.assertTrue(computers.contains(computer))) // Assert if computer is in the list
        .verifyComplete();
  }


  @Test
  @Order(1)
  void test_getComputersByEmployee() {
    Computer computer = createComputer("02:1A:2B:3C:4D:5E");

    ParameterizedTypeReference<List<Computer>> computerListType = new ParameterizedTypeReference<List<Computer>>() {};
    Mono<List<Computer>> getResult = webClient.get().uri("/api/computer/employee/123")
        .retrieve().bodyToMono(computerListType);

    // THEN
    StepVerifier.create(getResult)
        .assertNext(computers -> Assertions.assertTrue(computers.contains(computer))) // Assert if computer is in the list
        .verifyComplete();
  }

  @Test
  void test_getComputersByMacAddress() {
    Computer computer = createComputer("02:1A:2B:3C:4D:5E");

    Mono<Computer> getResult = webClient.get().uri("/api/computer/02:1A:2B:3C:4D:5E")
        .retrieve().bodyToMono(Computer.class);

    // THEN
    StepVerifier.create(getResult)
        .expectNext(computer) // Adjust this according to the actual structure of your response
        .verifyComplete();
  }

  @Test
  void test_updateComputer() {
    Computer computer = createComputer("02:1A:2B:3C:4D:5E");

    Mono<Computer> putResult = webClient.put().uri("/api/computer/02:1A:2B:3C:4D:5E/employee/456")
        .retrieve().bodyToMono(Computer.class);

    // THEN
    computer.setEmployeeAbbreviation("456");
    StepVerifier.create(putResult)
        .expectNext(computer) // Adjust this according to the actual structure of your response
        .verifyComplete();
  }

  @Test
  void test_deleteComputer() {
    createComputer("02:1A:2B:3C:4D:5E");
    webClient.delete().uri("/api/computer/02:1A:2B:3C:4D:5E")
        .retrieve()
        .toBodilessEntity()
        .flatMap(responseEntity -> Mono.just(responseEntity.getStatusCode())) // Extract status code
        .as(StepVerifier::create)
        .expectNext(HttpStatus.NO_CONTENT) // Check for a 204 status
        .verifyComplete();
  }

  private Computer createComputer(final String macAddress) {
    // GIVEN
    Computer computer = new Computer(macAddress, "Computer123", "192.168.1.1", "123", "Description");
    Mono<Computer> postResult = webClient.post().uri("/api/computer").contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(computer)).retrieve().bodyToMono(Computer.class);

    // THEN
    StepVerifier.create(postResult)
        .expectNext(computer)
        .verifyComplete();

    return computer;
  }
}

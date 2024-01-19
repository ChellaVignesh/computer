package com.samplecompany.computer.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import com.samplecompany.computer.dao.ComputerDao;
import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComputerRepositoryIntegrationTest {

  @Container
  private static final GenericContainer<?> h2Container = new GenericContainer<>("oscarfonts/h2")
      .withExposedPorts(1521)  // Expose the container's port
      .withEnv("H2_OPTIONS", "-ifNotExists")
      .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withPortBindings(
          List.of(new PortBinding(new Binding("localhost", "81"), new ExposedPort(1521)))));
  private final String jdbcUrl = "jdbc:h2:tcp://" + h2Container.getContainerIpAddress() +
      ":" + h2Container.getMappedPort(1521) + "/test";

  @Autowired
  private ComputerRepository computerRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void testFindByMacAddress() throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {

      // Create a Computer entity to insert
      ComputerDao computer = new ComputerDao("00:0A:0B:0C:0D:0E", "MyComputer", "192.168.1.1",
          "123", "Description");

      // Save the entity using the repository
      ComputerDao savedComputer = computerRepository.save(computer);

      // Query the database to check if the entity is saved
      ComputerDao retrievedComputer = computerRepository.findByMacAddress(
          savedComputer.getMacAddress()).orElse(null);

      // Assert that the entity is not null (indicating successful insertion)
      Assertions.assertNotNull(retrievedComputer);

      // Add more assertions based on your entity and expected data
      assertEquals("MyComputer", retrievedComputer.getComputerName());
      assertEquals("00:0A:0B:0C:0D:0E", retrievedComputer.getMacAddress());
      assertEquals("192.168.1.1", retrievedComputer.getIpAddress());
      assertEquals("123", retrievedComputer.getEmployeeAbbreviation());
      assertEquals("Description", retrievedComputer.getDescription());
    }
  }

  @Test
  void testFindByIPAddress() throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {

      // Create a Computer entity to insert
      ComputerDao computer = new ComputerDao("10:1A:1B:1C:1D:1E", "MyComputer1", "192.168.1.1",
          "234", "Description");

      // Save the entity using the repository
      ComputerDao savedComputer = computerRepository.save(computer);

      // Query the database to check if the entity is saved
      List<ComputerDao> retrievedComputer = computerRepository.findByEmployeeAbbreviation(
          savedComputer.getEmployeeAbbreviation());

      // Assert that the entity is not null (indicating successful insertion)
      Assertions.assertNotNull(retrievedComputer);
      assertEquals(1, retrievedComputer.size());

      // Add more assertions based on your entity and expected data
      assertEquals("MyComputer1", retrievedComputer.get(0).getComputerName());
      assertEquals("10:1A:1B:1C:1D:1E", retrievedComputer.get(0).getMacAddress());
      assertEquals("192.168.1.1", retrievedComputer.get(0).getIpAddress());
      assertEquals("234", retrievedComputer.get(0).getEmployeeAbbreviation());
      assertEquals("Description", retrievedComputer.get(0).getDescription());
    }
  }

  @Test
  @DirtiesContext
  @Transactional
  void testDeleteByMacAddress() throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {
      // Arrange: Save a ComputerDao entity with a specific mac address
      ComputerDao computer = new ComputerDao("20:2A:2B:2C:2D:2E", "MyComputer2", "192.168.1.1",
          "345", "Description");
      computerRepository.save(computer);

      // Flush the entity manager to synchronize the persistence context
      entityManager.flush();

      // Act: Delete entity by mac address
      computerRepository.deleteByMacAddress("20:2A:2B:2C:2D:2E");

      // Assert: Verify that the entity is deleted
      Optional<ComputerDao> deletedComputer = computerRepository.findByMacAddress(
          "20:2A:2B:2C:2D:2E");
      Assertions.assertFalse(deletedComputer.isPresent());
    }
  }

  @Test
  @DirtiesContext
    // Indicates that the context should be reloaded after this test method
  @Transactional
  void testUpdateByMacAddress() throws SQLException {
    try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "")) {
      // Arrange: Save a ComputerDao entity with a specific mac address and employee abbreviation
      ComputerDao computer = new ComputerDao("30:3A:3B:3C:3D:3E", "MyComputer3", "192.168.1.1",
          "456", "Description");
      computerRepository.save(computer);

      // Flush the entity manager to synchronize the persistence context
      entityManager.flush();

      // Query the database to check if the entity is saved
      ComputerDao retrievedComputer = computerRepository.findByMacAddress(
          computer.getMacAddress()).orElse(null);
      Assertions.assertNotNull(retrievedComputer);
      assertEquals("456", retrievedComputer.getEmployeeAbbreviation());

      // Act: Update entity by mac address
      computerRepository.updateByMacAddress("30:3A:3B:3C:3D:3E", "567");
      entityManager.flush();
      // Clear the entity manager to detach the entities
      entityManager.clear();

      // Assert: Verify that the entity is updated
      ComputerDao updatedComputer = computerRepository.findByMacAddress(
          computer.getMacAddress()).orElse(null);
      Assertions.assertNotNull(updatedComputer);
      assertEquals("567", updatedComputer.getEmployeeAbbreviation());
    }
  }
}

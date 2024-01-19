package com.samplecompany.computer.service.impl;

import com.samplecompany.computer.dao.ComputerDao;
import com.samplecompany.computer.mapper.ComputerMapper;
import com.samplecompany.computer.model.Computer;
import com.samplecompany.computer.repository.ComputerRepository;
import com.samplecompany.computer.service.NotificationService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdministratorServiceImplTest {

  @Mock
  private ComputerMapper computerMapper;

  @Mock
  private NotificationService notificationService;

  @Mock
  private ComputerRepository computerRepository;

  @InjectMocks
  private AdministratorServiceImpl administratorService;

  @Test
  void testGetAllComputers() {
    // Mocking repository behavior
    List<ComputerDao> computerDaoList = Arrays.asList(
        new ComputerDao("mac1", "comp1", "ip1", "emp1", "desc1"),
        new ComputerDao("mac2", "comp2", "ip2", "emp2", "desc2")
    );
    when(computerRepository.findAll()).thenReturn(computerDaoList);

    // Mocking mapper behavior
    List<Computer> expectedComputerList = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp1", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp2", "desc2")
    );
    when(computerMapper.mapToComputerList(computerDaoList)).thenReturn(expectedComputerList);

    // Actual method invocation
    List<Computer> result = administratorService.getAllComputers();

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).findAll();

    // Verifying that the mapper method was called
    verify(computerMapper, times(1)).mapToComputerList(computerDaoList);

    // Verifying the result
    assertEquals(expectedComputerList, result);
  }

  @Test
  void testNotifyIfThreeOrMoreAssignedComputers() {
    // Mocking repository behavior
    List<ComputerDao> computerDaoList = Arrays.asList(
        new ComputerDao("mac1", "comp1", "ip1", "emp1", "desc1"),
        new ComputerDao("mac2", "comp2", "ip2", "emp1", "desc2"),
        new ComputerDao("mac3", "comp3", "ip3", "emp1", "desc3")
    );
    when(computerRepository.findByEmployeeAbbreviation("emp1")).thenReturn(computerDaoList);

    // Mocking mapper behavior
    List<Computer> expectedComputerList = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp1", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp1", "desc2"),
        new Computer("mac3", "comp3", "ip3", "emp1", "desc3")
    );
    when(computerMapper.mapToComputerList(computerDaoList)).thenReturn(expectedComputerList);

    // Mocking notificationService behavior
    doNothing().when(notificationService).sendNotification("emp1");

    // Actual method invocation
    administratorService.notifyIfThreeOrMoreAssignedComputers("emp1");

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).findByEmployeeAbbreviation("emp1");

    // Verifying that the mapper method was called
    verify(computerMapper, times(1)).mapToComputerList(computerDaoList);

    // Verifying that the notificationService method was called
    verify(notificationService, times(1)).sendNotification("emp1");
  }

  @Test
  void testGetComputersByEmployee() {
    // Mocking repository behavior
    List<ComputerDao> computerDaoList = Arrays.asList(
        new ComputerDao("mac1", "comp1", "ip1", "emp1", "desc1"),
        new ComputerDao("mac2", "comp2", "ip2", "emp1", "desc2")
    );
    when(computerRepository.findByEmployeeAbbreviation("emp1")).thenReturn(computerDaoList);

    // Mocking mapper behavior
    List<Computer> expectedComputerList = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp1", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp1", "desc2")
    );
    when(computerMapper.mapToComputerList(computerDaoList)).thenReturn(expectedComputerList);

    // Actual method invocation
    List<Computer> result = administratorService.getComputersByEmployee("emp1");

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).findByEmployeeAbbreviation("emp1");

    // Verifying that the mapper method was called
    verify(computerMapper, times(1)).mapToComputerList(computerDaoList);

    // Verifying the result
    assertEquals(expectedComputerList, result);
  }

  @Test
  void testGetComputerByMacAddress() {
    // Mocking repository behavior
    ComputerDao computerDao = new ComputerDao("mac1", "comp1", "ip1", "emp1", "desc1");
    when(computerRepository.findByMacAddress("mac1")).thenReturn(Optional.of(computerDao));

    // Mocking mapper behavior
    Computer expectedComputer = new Computer("mac1", "comp1", "ip1", "emp1", "desc1");
    when(computerMapper.mapToComputer(computerDao)).thenReturn(expectedComputer);

    // Actual method invocation
    Computer result = administratorService.getComputerByMacAddress("mac1");

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).findByMacAddress("mac1");

    // Verifying that the mapper method was called
    verify(computerMapper, times(1)).mapToComputer(computerDao);

    // Verifying the result
    assertEquals(expectedComputer, result);
  }

  @Test
  void testRemoveComputer() {
    // Mocking repository behavior
    doNothing().when(computerRepository).deleteByMacAddress("mac1");

    // Actual method invocation
    administratorService.removeComputer("mac1");

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).deleteByMacAddress("mac1");
  }

  @Test
  void testAssignComputerToEmployee() {
    List<Computer> assignedComputers = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp2", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp2", "desc2"),
        new Computer("mac3", "comp3", "ip3", "emp2", "desc3")
    );
    when(administratorService.getComputersByEmployee("emp2")).thenReturn(assignedComputers);

    // Mocking repository behavior
    doNothing().when(computerRepository).updateByMacAddress("mac1", "emp2");

    // Mocking notificationService behavior
    doNothing().when(notificationService).sendNotification("emp2");

    // Actual method invocation
    administratorService.assignComputerToEmployee("mac1", "emp2");

    // Verifying that the repository method was called
    verify(computerRepository, times(1)).updateByMacAddress("mac1", "emp2");

    // Verifying that the notificationService method was called
    verify(notificationService, times(1)).sendNotification("emp2");
  }

  @Test
  void testNotifyIfThreeOrMoreAssignedComputers_NotTriggered() {
    // Mocking service behavior
    List<Computer> assignedComputers = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp1", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp1", "desc2")
    );
    when(administratorService.getComputersByEmployee("emp1")).thenReturn(assignedComputers);

    // Actual method invocation
    administratorService.notifyIfThreeOrMoreAssignedComputers("emp1");

    // Verifying that the notificationService method was not called
    verify(notificationService, times(0)).sendNotification(any());
  }

  @Test
  void testNotifyIfThreeOrMoreAssignedComputers_Triggered() {
    // Mocking service behavior
    List<Computer> assignedComputers = Arrays.asList(
        new Computer("mac1", "comp1", "ip1", "emp1", "desc1"),
        new Computer("mac2", "comp2", "ip2", "emp1", "desc2"),
        new Computer("mac3", "comp3", "ip3", "emp1", "desc3")
    );
    when(administratorService.getComputersByEmployee("emp1")).thenReturn(assignedComputers);

    // Actual method invocation
    administratorService.notifyIfThreeOrMoreAssignedComputers("emp1");

    // Verifying that the notificationService method was called
    verify(notificationService, times(1)).sendNotification("emp1");
  }

}


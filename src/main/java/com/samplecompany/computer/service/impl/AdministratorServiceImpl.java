package com.samplecompany.computer.service.impl;

import com.samplecompany.computer.errors.exception.ComputerNotFoundException;
import com.samplecompany.computer.mapper.ComputerMapper;
import com.samplecompany.computer.model.Computer;
import com.samplecompany.computer.repository.ComputerRepository;
import com.samplecompany.computer.service.AdministratorService;
import com.samplecompany.computer.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdministratorServiceImpl implements AdministratorService {
  @Autowired
  public AdministratorServiceImpl(ComputerMapper computerMapper, NotificationService notificationService,
      ComputerRepository computerRepository) {
    this.computerMapper = computerMapper;
    this.notificationService = notificationService;
    this.computerRepository = computerRepository;
  }

  private final ComputerMapper computerMapper;
  private final NotificationService notificationService;
  private final ComputerRepository computerRepository;
  public List<Computer> getAllComputers() {
    return computerMapper.mapToComputerList(computerRepository.findAll());
  }

  public List<Computer> getComputersByEmployee(String employeeAbbreviation) {
    return computerMapper.mapToComputerList(
        computerRepository.findByEmployeeAbbreviation(employeeAbbreviation));
  }

  public Computer getComputerByMacAddress(String macAddress) {
    return computerRepository.findByMacAddress(macAddress)
        .map(computerMapper::mapToComputer)
        .orElseThrow(ComputerNotFoundException::new);
  }

  @Transactional
  public void addComputer(Computer computer) {
    computerRepository.save(computerMapper.mapToComputerDao(computer));
    notifyIfThreeOrMoreAssignedComputers(computer.getEmployeeAbbreviation());
  }

  @Transactional
  public void removeComputer(String macAddress) {
    computerRepository.deleteByMacAddress(macAddress);
  }

  @Transactional
  public void assignComputerToEmployee(String macAddress, String newEmployeeAbbreviation) {
    computerRepository.updateByMacAddress(macAddress, newEmployeeAbbreviation);
    notifyIfThreeOrMoreAssignedComputers(newEmployeeAbbreviation);
  }

  public void notifyIfThreeOrMoreAssignedComputers(String employeeAbbreviation) {
    List<Computer> assignedComputers = getComputersByEmployee(employeeAbbreviation);
    if (assignedComputers.size() >= 3) {
      notificationService.sendNotification(employeeAbbreviation);
    }
  }
}

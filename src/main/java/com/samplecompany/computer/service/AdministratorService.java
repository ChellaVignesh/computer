package com.samplecompany.computer.service;

import com.samplecompany.computer.model.Computer;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AdministratorService {
  List<Computer> getAllComputers();

  List<Computer> getComputersByEmployee(String employeeAbbreviation);

  Computer getComputerByMacAddress(String macAddress);

  void removeComputer(String macAddress);

  void assignComputerToEmployee(String macAddress, String newEmployeeAbbreviation);

  void notifyIfThreeOrMoreAssignedComputers(String employeeAbbreviation);

  void addComputer(Computer computer);
}

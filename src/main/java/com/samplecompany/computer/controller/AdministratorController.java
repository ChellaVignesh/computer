package com.samplecompany.computer.controller;

import com.samplecompany.computer.annotation.Employee;
import com.samplecompany.computer.annotation.MacAddress;
import com.samplecompany.computer.errors.exception.ComputerNotFoundException;
import com.samplecompany.computer.model.Computer;
import com.samplecompany.computer.service.AdministratorService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/computer")
public class AdministratorController {
  Logger log = LoggerFactory.getLogger(AdministratorController.class);

  @Autowired
  private AdministratorService administratorService;

  @PostMapping
  public ResponseEntity<Computer> createComputer(@Valid @RequestBody Computer computer) {
    administratorService.addComputer(computer);
    return new ResponseEntity<>(computer, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Computer>> getAllComputers() {
    List<Computer> computers = administratorService.getAllComputers();
    return new ResponseEntity<>(computers, HttpStatus.OK);
  }

  @GetMapping("/employee/{employee}")
  public ResponseEntity<List<Computer>> getAllComputersByEmployee(@PathVariable @Employee @Nonnull String employee) {
    List<Computer> computers = administratorService.getComputersByEmployee(employee);
    return new ResponseEntity<>(computers, HttpStatus.OK);
  }

  @GetMapping("/{macAddress}")
  public ResponseEntity<Computer> getComputerByMacAddress(@PathVariable @MacAddress String macAddress) {
    return new ResponseEntity<>(administratorService.getComputerByMacAddress(macAddress), HttpStatus.OK);
  }

  @PutMapping("/{macAddress}/employee/{employee}")
  public ResponseEntity<Computer> updateComputer(@PathVariable @MacAddress String macAddress, @PathVariable @Employee @Nonnull String employee) {
    administratorService.assignComputerToEmployee(macAddress, employee);
    return new ResponseEntity<>(administratorService.getComputerByMacAddress(macAddress), HttpStatus.OK);
  }

  @DeleteMapping("/{macAddress}")
  public ResponseEntity<Void> deleteComputer(@PathVariable @MacAddress String macAddress) {
    administratorService.removeComputer(macAddress);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

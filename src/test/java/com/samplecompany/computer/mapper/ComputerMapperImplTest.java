package com.samplecompany.computer.mapper;

import com.samplecompany.computer.dao.ComputerDao;
import com.samplecompany.computer.model.Computer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComputerMapperImplTest {

  @Test
  void testMapToComputerDao() {
    // Given
    ComputerMapper computerMapper = Mappers.getMapper(ComputerMapper.class);
    Computer computer = new Computer("mac123", "MyComputer", "192.168.1.1", "emp123", "Description");

    // When
    ComputerDao computerDao = computerMapper.mapToComputerDao(computer);

    // Then
    assertEquals(computer.getMacAddress(), computerDao.getMacAddress());
    assertEquals(computer.getComputerName(), computerDao.getComputerName());
    assertEquals(computer.getIpAddress(), computerDao.getIpAddress());
    assertEquals(computer.getEmployeeAbbreviation(), computerDao.getEmployeeAbbreviation());
    assertEquals(computer.getDescription(), computerDao.getDescription());
  }

  @Test
  void testMapToComputer() {
    // Given
    ComputerMapper computerMapper = Mappers.getMapper(ComputerMapper.class);
    ComputerDao computerDao = new ComputerDao("mac123", "MyComputer", "192.168.1.1", "emp123", "Description");

    // When
    Computer computer = computerMapper.mapToComputer(computerDao);

    // Then
    assertEquals(computerDao.getMacAddress(), computer.getMacAddress());
    assertEquals(computerDao.getComputerName(), computer.getComputerName());
    assertEquals(computerDao.getIpAddress(), computer.getIpAddress());
    assertEquals(computerDao.getEmployeeAbbreviation(), computer.getEmployeeAbbreviation());
    assertEquals(computerDao.getDescription(), computer.getDescription());
  }

  @Test
  void testMapToComputerList() {
    // Given
    ComputerMapper computerMapper = Mappers.getMapper(ComputerMapper.class);
    ComputerDao computerDao1 = new ComputerDao("mac123", "MyComputer", "192.168.1.1", "emp123", "Description");
    ComputerDao computerDao2 = new ComputerDao("mac456", "YourComputer", "192.168.1.2", "emp456", "Another Description");
    List<ComputerDao> computerDaoList = Arrays.asList(computerDao1, computerDao2);

    // When
    List<Computer> computerList = computerMapper.mapToComputerList(computerDaoList);

    // Then
    assertEquals(computerDaoList.size(), computerList.size());
    // Additional assertions for each item in the list
    // ...
  }
}


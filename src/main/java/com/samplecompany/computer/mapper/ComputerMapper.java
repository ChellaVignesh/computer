package com.samplecompany.computer.mapper;

import com.samplecompany.computer.dao.ComputerDao;
import com.samplecompany.computer.model.Computer;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComputerMapper {
  ComputerDao mapToComputerDao(Computer computer);
  Computer mapToComputer(ComputerDao computerDao);
  List<Computer> mapToComputerList(List<ComputerDao> computerDaoList);
}

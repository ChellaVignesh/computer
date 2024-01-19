package com.samplecompany.computer.repository;

import com.samplecompany.computer.dao.ComputerDao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerRepository extends JpaRepository<ComputerDao, Long> {

  List<ComputerDao> findByEmployeeAbbreviation(final String employeeAbbreviation);

  Optional<ComputerDao> findByMacAddress(final String macAddress);

  void deleteByMacAddress(final String macAddress);

  @Modifying
  @Query("UPDATE ComputerDao c SET c.employeeAbbreviation = :newEmployeeAbbreviation WHERE c.macAddress = :macAddress")
  void updateByMacAddress(@Param("macAddress") String macAddress, @Param("newEmployeeAbbreviation") String newEmployeeAbbreviation);
}

package com.samplecompany.computer.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPUTER")
public class ComputerDao {
    @Id
    @Column(name = "mac_address", columnDefinition = "VARCHAR(17)", unique = true, nullable = false)
    private String macAddress;

    @Column(columnDefinition = "VARCHAR(255)")
    private String computerName;

    @Column(columnDefinition = "VARCHAR(15)")
    private String ipAddress;

    @Column(columnDefinition = "VARCHAR(3)")
    private String employeeAbbreviation;

    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    public ComputerDao() {}
    public ComputerDao(String macAddress, String computerName, String ipAddress,
        String employeeAbbreviation, String description) {
        this.macAddress = macAddress;
        this.computerName = computerName;
        this.ipAddress = ipAddress;
        this.employeeAbbreviation = employeeAbbreviation;
        this.description = description;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmployeeAbbreviation() {
        return employeeAbbreviation;
    }

    public void setEmployeeAbbreviation(String employeeAbbreviation) {
        this.employeeAbbreviation = employeeAbbreviation;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}


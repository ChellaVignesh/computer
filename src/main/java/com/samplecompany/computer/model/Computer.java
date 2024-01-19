package com.samplecompany.computer.model;
import com.samplecompany.computer.annotation.Employee;
import com.samplecompany.computer.annotation.MacAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

public class Computer {

    @NotBlank(message = "Mac Address cannot be blank")
    @MacAddress
    @Schema(description = "Mac Address cannot be blank and has to be in XX:XX:XX:XX:XX:XX pattern")
    private String macAddress;

    @NotBlank(message = "Computer Name cannot be blank")
    private String computerName;

    @NotBlank(message = "IP Address cannot be blank")
    @Pattern(regexp = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b", message = "Invalid IP Address format")
    private String ipAddress;

    @Employee
    @Schema(description = "Employee Abbreviation has to be exactly 3 chars")
    private String employeeAbbreviation;

    private String description;

    public Computer() {
    }

    public Computer(String macAddress, String computerName, String ipAddress,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Computer computer = (Computer) o;
        return Objects.equals(macAddress, computer.macAddress) &&
            Objects.equals(computerName, computer.computerName) &&
            Objects.equals(ipAddress, computer.ipAddress) &&
            Objects.equals(employeeAbbreviation, computer.employeeAbbreviation) &&
            Objects.equals(description, computer.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddress, computerName, ipAddress, employeeAbbreviation, description);
    }
}

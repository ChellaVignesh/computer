package com.samplecompany.computer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samplecompany.computer.model.Computer;
import com.samplecompany.computer.service.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdministratorControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  private AdministratorService administratorService;

  @InjectMocks
  private AdministratorController administratorController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(administratorController).build();
  }

  @Test
  void testCreateComputer() throws Exception {
    Computer computer = new Computer("02:1A:2B:3C:4D:5E", "Computer123", "192.168.1.1", "123", "Description");

    ResultActions result = mockMvc.perform(post("/api/computer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(computer)));

    result.andExpect(status().isCreated())
        .andExpect(jsonPath("$.macAddress").value("02:1A:2B:3C:4D:5E"))
        .andExpect(jsonPath("$.computerName").value("Computer123"))
        .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"))
        .andExpect(jsonPath("$.employeeAbbreviation").value("123"))
        .andExpect(jsonPath("$.description").value("Description"));
  }

  @Test
  void testGetAllComputers() throws Exception {
    List<Computer> computers = Arrays.asList(
        new Computer("mac123", "Computer123", "192.168.1.1", "emp123", "Description"),
        new Computer("mac456", "Computer456", "192.168.1.2", "emp456", "Description456")
    );
    when(administratorService.getAllComputers()).thenReturn(computers);

    ResultActions result = mockMvc.perform(get("/api/computer"));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$[0].macAddress").value("mac123"))
        .andExpect(jsonPath("$[1].macAddress").value("mac456"))
        .andExpect(jsonPath("$[0].computerName").value("Computer123"))
        .andExpect(jsonPath("$[1].computerName").value("Computer456"))
        .andExpect(jsonPath("$[0].ipAddress").value("192.168.1.1"))
        .andExpect(jsonPath("$[1].ipAddress").value("192.168.1.2"))
        .andExpect(jsonPath("$[0].employeeAbbreviation").value("emp123"))
        .andExpect(jsonPath("$[1].employeeAbbreviation").value("emp456"))
        .andExpect(jsonPath("$[0].description").value("Description"))
        .andExpect(jsonPath("$[1].description").value("Description456"));
  }

  @Test
  void testGetAllComputersByEmployee() throws Exception {
    List<Computer> computers = Arrays.asList(
        new Computer("mac123", "Computer123", "192.168.1.1", "emp123", "Description"),
        new Computer("mac456", "Computer456", "192.168.1.2", "emp123", "Description456")
    );
    when(administratorService.getComputersByEmployee(eq("emp123"))).thenReturn(computers);

    ResultActions result = mockMvc.perform(get("/api/computer/employee/emp123"));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$[0].macAddress").value("mac123"))
        .andExpect(jsonPath("$[1].macAddress").value("mac456"))
        .andExpect(jsonPath("$[0].computerName").value("Computer123"))
        .andExpect(jsonPath("$[1].computerName").value("Computer456"))
        .andExpect(jsonPath("$[0].ipAddress").value("192.168.1.1"))
        .andExpect(jsonPath("$[1].ipAddress").value("192.168.1.2"))
        .andExpect(jsonPath("$[0].employeeAbbreviation").value("emp123"))
        .andExpect(jsonPath("$[1].employeeAbbreviation").value("emp123"))
        .andExpect(jsonPath("$[0].description").value("Description"))
        .andExpect(jsonPath("$[1].description").value("Description456"));
  }

  @Test
  void testGetComputerByMacAddress() throws Exception {
    Computer computer = new Computer("mac123", "Computer123", "192.168.1.1", "emp123", "Description");
    when(administratorService.getComputerByMacAddress(eq("mac123"))).thenReturn(computer);

    ResultActions result = mockMvc.perform(get("/api/computer/mac123"));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.macAddress").value("mac123"))
        .andExpect(jsonPath("$.computerName").value("Computer123"))
        .andExpect(jsonPath("$.ipAddress").value("192.168.1.1"))
        .andExpect(jsonPath("$.employeeAbbreviation").value("emp123"))
        .andExpect(jsonPath("$.description").value("Description"));
  }

  @Test
  void testUpdateComputer() throws Exception {
    Computer computer = new Computer("mac123", "Computer123", "192.168.1.1", "emp456", "Description");
    when(administratorService.getComputerByMacAddress(eq("mac123"))).thenReturn(computer);

    ResultActions result = mockMvc.perform(put("/api/computer/mac123/employee/emp456"));
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.employeeAbbreviation").value("emp456"));
  }

  @Test
  void testDeleteComputer() throws Exception {
    ResultActions result = mockMvc.perform(delete("/api/computer/mac123"));
    result.andExpect(status().isNoContent());
  }

  @Test
  void testInvalidMacAddress_OnAddComputer() throws Exception {
    Computer computer = new Computer("invalid_Mac_Address", "Computer123", "192.168.1.1", "123",
        "Description");

    ResultActions result = mockMvc.perform(post("/api/computer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(computer)));

    result.andExpect(status().isBadRequest());
  }

  @Test
  void testInvalidEmployeeId_OnAddComputer() throws Exception {
    Computer computer = new Computer("02:1A:2B:3C:4D:5E", "Computer123", "192.168.1.1", "E123",
        "Description");

    ResultActions result = mockMvc.perform(post("/api/computer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(computer)));

    result.andExpect(status().isBadRequest());
  }

  @Test
  void testInvalidIPAddress_OnAddComputer() throws Exception {
    Computer computer = new Computer("02:1A:2B:3C:4D:5E", "Computer123", "Invalid_IP_Address", "123",
        "Description");

    ResultActions result = mockMvc.perform(post("/api/computer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(computer)));

    result.andExpect(status().isBadRequest());
  }

  @Test
  void testEmptyComputerName_OnAddComputer() throws Exception {
    Computer computer = new Computer("02:1A:2B:3C:4D:5E", null, "192.168.1.1", "123",
        "Description");

    ResultActions result = mockMvc.perform(post("/api/computer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(computer)));

    result.andExpect(status().isBadRequest());
  }
}


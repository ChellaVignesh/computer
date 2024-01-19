package com.samplecompany.computer.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface NotificationService {
  void sendNotification(final String employeeAbbreviation);
}

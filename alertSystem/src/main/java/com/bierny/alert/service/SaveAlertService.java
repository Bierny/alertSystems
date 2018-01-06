package com.bierny.alert.service;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentServiceEntity;
import com.bierny.alert.repository.IncidentRepository;
import com.bierny.alert.repository.IncidentServiceRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by damian.biernat on 03.01.2018.
 */
@Component
public class SaveAlertService {

  @Autowired
  private IncidentRepository incidentRepository;
  @Autowired
  private UserService userService;

  @Autowired
  private IncidentServiceRepository incidentServiceRepository;

  public void saveAlertToDatabase(String message) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Incident incident = objectMapper.readValue(message,Incident.class);
    java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
    incident.setFillingDate(date);
    QueueIncidentsSingleton.getInstance().addIncident(incidentRepository.save(incident));
  }
}

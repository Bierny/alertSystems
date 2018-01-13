package com.bierny.alert.repository;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


@Repository
public interface IncidentRepository extends JpaRepository<Incident,Long> {

  List<Incident> findByStatus(IncidentStatus incidentStatus);
    
}


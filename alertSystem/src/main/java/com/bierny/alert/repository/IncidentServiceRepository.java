package com.bierny.alert.repository;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Incident entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncidentServiceRepository extends JpaRepository<IncidentServiceEntity,Long> {
    
}

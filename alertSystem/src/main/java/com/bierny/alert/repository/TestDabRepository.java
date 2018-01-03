package com.bierny.alert.repository;

import com.bierny.alert.domain.TestDab;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestDab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestDabRepository extends JpaRepository<TestDab,Long> {
    
}

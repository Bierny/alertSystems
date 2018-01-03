package com.bierny.alert.service;

import com.bierny.alert.domain.TestDab;
import com.bierny.alert.repository.TestDabRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TestDab.
 */
@Service
@Transactional
public class TestDabService {

    private final Logger log = LoggerFactory.getLogger(TestDabService.class);

    private final TestDabRepository testDabRepository;

    public TestDabService(TestDabRepository testDabRepository) {
        this.testDabRepository = testDabRepository;
    }

    /**
     * Save a testDab.
     *
     * @param testDab the entity to save
     * @return the persisted entity
     */
    public TestDab save(TestDab testDab) {
        log.debug("Request to save TestDab : {}", testDab);
        return testDabRepository.save(testDab);
    }

    /**
     *  Get all the testDabs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TestDab> findAll(Pageable pageable) {
        log.debug("Request to get all TestDabs");
        return testDabRepository.findAll(pageable);
    }

    /**
     *  Get one testDab by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TestDab findOne(Long id) {
        log.debug("Request to get TestDab : {}", id);
        return testDabRepository.findOne(id);
    }

    /**
     *  Delete the  testDab by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TestDab : {}", id);
        testDabRepository.delete(id);
    }
}

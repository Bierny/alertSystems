package com.bierny.alert.web.rest;

import com.bierny.alert.domain.Incident;
import com.bierny.alert.domain.IncidentServiceEntity;
import com.bierny.alert.domain.TestDab;
import com.bierny.alert.repository.IncidentRepository;
import com.bierny.alert.service.QueueIncidentsSingleton;
import com.bierny.alert.service.TestDabService;
import com.bierny.alert.web.rest.util.HeaderUtil;
import com.bierny.alert.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * REST controller for managing TestDab.
 */
@RestController
@RequestMapping("/api")
public class IncidentsResource {

    private final Logger log = LoggerFactory.getLogger(IncidentsResource.class);




    /**
     * GET  /test-dabs : get all the testDabs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testDabs in body
     */
    @GetMapping("/alerts")
    @Timed
    public ResponseEntity<List<Incident>> getAllTestDabs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestDabs");
        Queue queue =QueueIncidentsSingleton.getInstance().getQueue();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(new ArrayList(queue)));
    }

    /**
     * GET  /test-dabs/:id : get the "id" testDab.
     *
     * @param id the id of the testDab to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testDab, or with status 404 (Not Found)
     */
    @GetMapping("/alerts/{id}")
    @Timed
    public ResponseEntity<Incident> getTestDab(@PathVariable Long id) {
        log.debug("REST request to get TestDab : {}", id);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(QueueIncidentsSingleton.getInstance().peekFirstIncident()));
    }

}

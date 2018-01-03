package com.bierny.alert.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bierny.alert.domain.Incident;
import com.bierny.alert.service.IncidentService;
import com.bierny.alert.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Incident.
 */
@RestController
@RequestMapping("/api")
public class IncidentResource {

    private final Logger log = LoggerFactory.getLogger(IncidentResource.class);

    private static final String ENTITY_NAME = "incident";

    private final IncidentService incidentService;

    public IncidentResource(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    /**
     * POST  /incidents : Create a new incident.
     *
     * @param incident the incident to create
     * @return the ResponseEntity with status 201 (Created) and with body the new incident, or with status 400 (Bad Request) if the incident has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/incidents")
    @Timed
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) throws URISyntaxException {
        log.debug("REST request to save Incident : {}", incident);
        if (incident.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new incident cannot already have an ID")).body(null);
        }
        Incident result = incidentService.save(incident);
        return ResponseEntity.created(new URI("/api/incidents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incidents : Updates an existing incident.
     *
     * @param incident the incident to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated incident,
     * or with status 400 (Bad Request) if the incident is not valid,
     * or with status 500 (Internal Server Error) if the incident couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/incidents")
    @Timed
    public ResponseEntity<Incident> updateIncident(@RequestBody Incident incident) throws URISyntaxException {
        log.debug("REST request to update Incident : {}", incident);
        if (incident.getId() == null) {
            return createIncident(incident);
        }
        Incident result = incidentService.save(incident);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, incident.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incidents : get all the incidents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of incidents in body
     */
    @GetMapping("/incidents")
    @Timed
    public List<Incident> getAllIncidents() {
        log.debug("REST request to get all Incidents");
        return incidentService.findAll();
    }

    /**
     * GET  /incidents/:id : get the "id" incident.
     *
     * @param id the id of the incident to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the incident, or with status 404 (Not Found)
     */
    @GetMapping("/incidents/{id}")
    @Timed
    public ResponseEntity<Incident> getIncident(@PathVariable Long id) {
        log.debug("REST request to get Incident : {}", id);
        Incident incident = incidentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(incident));
    }

    /**
     * DELETE  /incidents/:id : delete the "id" incident.
     *
     * @param id the id of the incident to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/incidents/{id}")
    @Timed
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        log.debug("REST request to delete Incident : {}", id);
        incidentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

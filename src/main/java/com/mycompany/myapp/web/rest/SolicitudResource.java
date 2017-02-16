package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Solicitud;

import com.mycompany.myapp.repository.SolicitudRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
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
 * REST controller for managing Solicitud.
 */
@RestController
@RequestMapping("/api")
public class SolicitudResource {

    private final Logger log = LoggerFactory.getLogger(SolicitudResource.class);

    private static final String ENTITY_NAME = "solicitud";
        
    private final SolicitudRepository solicitudRepository;

    public SolicitudResource(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * POST  /solicituds : Create a new solicitud.
     *
     * @param solicitud the solicitud to create
     * @return the ResponseEntity with status 201 (Created) and with body the new solicitud, or with status 400 (Bad Request) if the solicitud has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/solicituds")
    @Timed
    public ResponseEntity<Solicitud> createSolicitud(@RequestBody Solicitud solicitud) throws URISyntaxException {
        log.debug("REST request to save Solicitud : {}", solicitud);
        if (solicitud.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new solicitud cannot already have an ID")).body(null);
        }
        Solicitud result = solicitudRepository.save(solicitud);
        return ResponseEntity.created(new URI("/api/solicituds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /solicituds : Updates an existing solicitud.
     *
     * @param solicitud the solicitud to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated solicitud,
     * or with status 400 (Bad Request) if the solicitud is not valid,
     * or with status 500 (Internal Server Error) if the solicitud couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/solicituds")
    @Timed
    public ResponseEntity<Solicitud> updateSolicitud(@RequestBody Solicitud solicitud) throws URISyntaxException {
        log.debug("REST request to update Solicitud : {}", solicitud);
        if (solicitud.getId() == null) {
            return createSolicitud(solicitud);
        }
        Solicitud result = solicitudRepository.save(solicitud);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, solicitud.getId().toString()))
            .body(result);
    }

    /**
     * GET  /solicituds : get all the solicituds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of solicituds in body
     */
    @GetMapping("/solicituds")
    @Timed
    public List<Solicitud> getAllSolicituds() {
        log.debug("REST request to get all Solicituds");
        List<Solicitud> solicituds = solicitudRepository.findAllWithEagerRelationships();
        return solicituds;
    }

    /**
     * GET  /solicituds/:id : get the "id" solicitud.
     *
     * @param id the id of the solicitud to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the solicitud, or with status 404 (Not Found)
     */
    @GetMapping("/solicituds/{id}")
    @Timed
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable Long id) {
        log.debug("REST request to get Solicitud : {}", id);
        Solicitud solicitud = solicitudRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(solicitud));
    }

    /**
     * DELETE  /solicituds/:id : delete the "id" solicitud.
     *
     * @param id the id of the solicitud to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/solicituds/{id}")
    @Timed
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        log.debug("REST request to delete Solicitud : {}", id);
        solicitudRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

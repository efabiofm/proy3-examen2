package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Frecuencia;

import com.mycompany.myapp.repository.FrecuenciaRepository;
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
 * REST controller for managing Frecuencia.
 */
@RestController
@RequestMapping("/api")
public class FrecuenciaResource {

    private final Logger log = LoggerFactory.getLogger(FrecuenciaResource.class);

    private static final String ENTITY_NAME = "frecuencia";
        
    private final FrecuenciaRepository frecuenciaRepository;

    public FrecuenciaResource(FrecuenciaRepository frecuenciaRepository) {
        this.frecuenciaRepository = frecuenciaRepository;
    }

    /**
     * POST  /frecuencias : Create a new frecuencia.
     *
     * @param frecuencia the frecuencia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new frecuencia, or with status 400 (Bad Request) if the frecuencia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/frecuencias")
    @Timed
    public ResponseEntity<Frecuencia> createFrecuencia(@RequestBody Frecuencia frecuencia) throws URISyntaxException {
        log.debug("REST request to save Frecuencia : {}", frecuencia);
        if (frecuencia.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new frecuencia cannot already have an ID")).body(null);
        }
        Frecuencia result = frecuenciaRepository.save(frecuencia);
        return ResponseEntity.created(new URI("/api/frecuencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /frecuencias : Updates an existing frecuencia.
     *
     * @param frecuencia the frecuencia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated frecuencia,
     * or with status 400 (Bad Request) if the frecuencia is not valid,
     * or with status 500 (Internal Server Error) if the frecuencia couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/frecuencias")
    @Timed
    public ResponseEntity<Frecuencia> updateFrecuencia(@RequestBody Frecuencia frecuencia) throws URISyntaxException {
        log.debug("REST request to update Frecuencia : {}", frecuencia);
        if (frecuencia.getId() == null) {
            return createFrecuencia(frecuencia);
        }
        Frecuencia result = frecuenciaRepository.save(frecuencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, frecuencia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /frecuencias : get all the frecuencias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of frecuencias in body
     */
    @GetMapping("/frecuencias")
    @Timed
    public List<Frecuencia> getAllFrecuencias() {
        log.debug("REST request to get all Frecuencias");
        List<Frecuencia> frecuencias = frecuenciaRepository.findAll();
        return frecuencias;
    }

    /**
     * GET  /frecuencias/:id : get the "id" frecuencia.
     *
     * @param id the id of the frecuencia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the frecuencia, or with status 404 (Not Found)
     */
    @GetMapping("/frecuencias/{id}")
    @Timed
    public ResponseEntity<Frecuencia> getFrecuencia(@PathVariable Long id) {
        log.debug("REST request to get Frecuencia : {}", id);
        Frecuencia frecuencia = frecuenciaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(frecuencia));
    }

    /**
     * DELETE  /frecuencias/:id : delete the "id" frecuencia.
     *
     * @param id the id of the frecuencia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/frecuencias/{id}")
    @Timed
    public ResponseEntity<Void> deleteFrecuencia(@PathVariable Long id) {
        log.debug("REST request to delete Frecuencia : {}", id);
        frecuenciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

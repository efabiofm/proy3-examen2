package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Examen2App;

import com.mycompany.myapp.domain.Solicitud;
import com.mycompany.myapp.repository.SolicitudRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SolicitudResource REST controller.
 *
 * @see SolicitudResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Examen2App.class)
public class SolicitudResourceIntTest {

    private static final ZonedDateTime DEFAULT_FECHA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ES_RECURSIVO = false;
    private static final Boolean UPDATED_ES_RECURSIVO = true;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSolicitudMockMvc;

    private Solicitud solicitud;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SolicitudResource solicitudResource = new SolicitudResource(solicitudRepository);
        this.restSolicitudMockMvc = MockMvcBuilders.standaloneSetup(solicitudResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitud createEntity(EntityManager em) {
        Solicitud solicitud = new Solicitud()
                .fechaEntrega(DEFAULT_FECHA_ENTREGA)
                .esRecursivo(DEFAULT_ES_RECURSIVO);
        return solicitud;
    }

    @Before
    public void initTest() {
        solicitud = createEntity(em);
    }

    @Test
    @Transactional
    public void createSolicitud() throws Exception {
        int databaseSizeBeforeCreate = solicitudRepository.findAll().size();

        // Create the Solicitud

        restSolicitudMockMvc.perform(post("/api/solicituds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitud)))
            .andExpect(status().isCreated());

        // Validate the Solicitud in the database
        List<Solicitud> solicitudList = solicitudRepository.findAll();
        assertThat(solicitudList).hasSize(databaseSizeBeforeCreate + 1);
        Solicitud testSolicitud = solicitudList.get(solicitudList.size() - 1);
        assertThat(testSolicitud.getFechaEntrega()).isEqualTo(DEFAULT_FECHA_ENTREGA);
        assertThat(testSolicitud.isEsRecursivo()).isEqualTo(DEFAULT_ES_RECURSIVO);
    }

    @Test
    @Transactional
    public void createSolicitudWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solicitudRepository.findAll().size();

        // Create the Solicitud with an existing ID
        Solicitud existingSolicitud = new Solicitud();
        existingSolicitud.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitudMockMvc.perform(post("/api/solicituds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSolicitud)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Solicitud> solicitudList = solicitudRepository.findAll();
        assertThat(solicitudList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSolicituds() throws Exception {
        // Initialize the database
        solicitudRepository.saveAndFlush(solicitud);

        // Get all the solicitudList
        restSolicitudMockMvc.perform(get("/api/solicituds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitud.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(sameInstant(DEFAULT_FECHA_ENTREGA))))
            .andExpect(jsonPath("$.[*].esRecursivo").value(hasItem(DEFAULT_ES_RECURSIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getSolicitud() throws Exception {
        // Initialize the database
        solicitudRepository.saveAndFlush(solicitud);

        // Get the solicitud
        restSolicitudMockMvc.perform(get("/api/solicituds/{id}", solicitud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solicitud.getId().intValue()))
            .andExpect(jsonPath("$.fechaEntrega").value(sameInstant(DEFAULT_FECHA_ENTREGA)))
            .andExpect(jsonPath("$.esRecursivo").value(DEFAULT_ES_RECURSIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitud() throws Exception {
        // Get the solicitud
        restSolicitudMockMvc.perform(get("/api/solicituds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitud() throws Exception {
        // Initialize the database
        solicitudRepository.saveAndFlush(solicitud);
        int databaseSizeBeforeUpdate = solicitudRepository.findAll().size();

        // Update the solicitud
        Solicitud updatedSolicitud = solicitudRepository.findOne(solicitud.getId());
        updatedSolicitud
                .fechaEntrega(UPDATED_FECHA_ENTREGA)
                .esRecursivo(UPDATED_ES_RECURSIVO);

        restSolicitudMockMvc.perform(put("/api/solicituds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSolicitud)))
            .andExpect(status().isOk());

        // Validate the Solicitud in the database
        List<Solicitud> solicitudList = solicitudRepository.findAll();
        assertThat(solicitudList).hasSize(databaseSizeBeforeUpdate);
        Solicitud testSolicitud = solicitudList.get(solicitudList.size() - 1);
        assertThat(testSolicitud.getFechaEntrega()).isEqualTo(UPDATED_FECHA_ENTREGA);
        assertThat(testSolicitud.isEsRecursivo()).isEqualTo(UPDATED_ES_RECURSIVO);
    }

    @Test
    @Transactional
    public void updateNonExistingSolicitud() throws Exception {
        int databaseSizeBeforeUpdate = solicitudRepository.findAll().size();

        // Create the Solicitud

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSolicitudMockMvc.perform(put("/api/solicituds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitud)))
            .andExpect(status().isCreated());

        // Validate the Solicitud in the database
        List<Solicitud> solicitudList = solicitudRepository.findAll();
        assertThat(solicitudList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSolicitud() throws Exception {
        // Initialize the database
        solicitudRepository.saveAndFlush(solicitud);
        int databaseSizeBeforeDelete = solicitudRepository.findAll().size();

        // Get the solicitud
        restSolicitudMockMvc.perform(delete("/api/solicituds/{id}", solicitud.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Solicitud> solicitudList = solicitudRepository.findAll();
        assertThat(solicitudList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Solicitud.class);
    }
}

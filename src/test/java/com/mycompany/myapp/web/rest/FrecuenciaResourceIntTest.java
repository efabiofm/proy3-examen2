package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Examen2App;

import com.mycompany.myapp.domain.Frecuencia;
import com.mycompany.myapp.repository.FrecuenciaRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FrecuenciaResource REST controller.
 *
 * @see FrecuenciaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Examen2App.class)
public class FrecuenciaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private FrecuenciaRepository frecuenciaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFrecuenciaMockMvc;

    private Frecuencia frecuencia;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            FrecuenciaResource frecuenciaResource = new FrecuenciaResource(frecuenciaRepository);
        this.restFrecuenciaMockMvc = MockMvcBuilders.standaloneSetup(frecuenciaResource)
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
    public static Frecuencia createEntity(EntityManager em) {
        Frecuencia frecuencia = new Frecuencia()
                .nombre(DEFAULT_NOMBRE);
        return frecuencia;
    }

    @Before
    public void initTest() {
        frecuencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createFrecuencia() throws Exception {
        int databaseSizeBeforeCreate = frecuenciaRepository.findAll().size();

        // Create the Frecuencia

        restFrecuenciaMockMvc.perform(post("/api/frecuencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frecuencia)))
            .andExpect(status().isCreated());

        // Validate the Frecuencia in the database
        List<Frecuencia> frecuenciaList = frecuenciaRepository.findAll();
        assertThat(frecuenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Frecuencia testFrecuencia = frecuenciaList.get(frecuenciaList.size() - 1);
        assertThat(testFrecuencia.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createFrecuenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = frecuenciaRepository.findAll().size();

        // Create the Frecuencia with an existing ID
        Frecuencia existingFrecuencia = new Frecuencia();
        existingFrecuencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrecuenciaMockMvc.perform(post("/api/frecuencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFrecuencia)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Frecuencia> frecuenciaList = frecuenciaRepository.findAll();
        assertThat(frecuenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFrecuencias() throws Exception {
        // Initialize the database
        frecuenciaRepository.saveAndFlush(frecuencia);

        // Get all the frecuenciaList
        restFrecuenciaMockMvc.perform(get("/api/frecuencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frecuencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getFrecuencia() throws Exception {
        // Initialize the database
        frecuenciaRepository.saveAndFlush(frecuencia);

        // Get the frecuencia
        restFrecuenciaMockMvc.perform(get("/api/frecuencias/{id}", frecuencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(frecuencia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFrecuencia() throws Exception {
        // Get the frecuencia
        restFrecuenciaMockMvc.perform(get("/api/frecuencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFrecuencia() throws Exception {
        // Initialize the database
        frecuenciaRepository.saveAndFlush(frecuencia);
        int databaseSizeBeforeUpdate = frecuenciaRepository.findAll().size();

        // Update the frecuencia
        Frecuencia updatedFrecuencia = frecuenciaRepository.findOne(frecuencia.getId());
        updatedFrecuencia
                .nombre(UPDATED_NOMBRE);

        restFrecuenciaMockMvc.perform(put("/api/frecuencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFrecuencia)))
            .andExpect(status().isOk());

        // Validate the Frecuencia in the database
        List<Frecuencia> frecuenciaList = frecuenciaRepository.findAll();
        assertThat(frecuenciaList).hasSize(databaseSizeBeforeUpdate);
        Frecuencia testFrecuencia = frecuenciaList.get(frecuenciaList.size() - 1);
        assertThat(testFrecuencia.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingFrecuencia() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaRepository.findAll().size();

        // Create the Frecuencia

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFrecuenciaMockMvc.perform(put("/api/frecuencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(frecuencia)))
            .andExpect(status().isCreated());

        // Validate the Frecuencia in the database
        List<Frecuencia> frecuenciaList = frecuenciaRepository.findAll();
        assertThat(frecuenciaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFrecuencia() throws Exception {
        // Initialize the database
        frecuenciaRepository.saveAndFlush(frecuencia);
        int databaseSizeBeforeDelete = frecuenciaRepository.findAll().size();

        // Get the frecuencia
        restFrecuenciaMockMvc.perform(delete("/api/frecuencias/{id}", frecuencia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Frecuencia> frecuenciaList = frecuenciaRepository.findAll();
        assertThat(frecuenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Frecuencia.class);
    }
}

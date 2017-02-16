package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Examen2App;

import com.mycompany.myapp.domain.Estado;
import com.mycompany.myapp.repository.EstadoRepository;
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
 * Test class for the EstadoResource REST controller.
 *
 * @see EstadoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Examen2App.class)
public class EstadoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEstadoMockMvc;

    private Estado estado;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EstadoResource estadoResource = new EstadoResource(estadoRepository);
        this.restEstadoMockMvc = MockMvcBuilders.standaloneSetup(estadoResource)
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
    public static Estado createEntity(EntityManager em) {
        Estado estado = new Estado()
                .nombre(DEFAULT_NOMBRE);
        return estado;
    }

    @Before
    public void initTest() {
        estado = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstado() throws Exception {
        int databaseSizeBeforeCreate = estadoRepository.findAll().size();

        // Create the Estado

        restEstadoMockMvc.perform(post("/api/estados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estado)))
            .andExpect(status().isCreated());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeCreate + 1);
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createEstadoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estadoRepository.findAll().size();

        // Create the Estado with an existing ID
        Estado existingEstado = new Estado();
        existingEstado.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoMockMvc.perform(post("/api/estados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEstado)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEstados() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get all the estadoList
        restEstadoMockMvc.perform(get("/api/estados?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstado() throws Exception {
        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Update the estado
        Estado updatedEstado = estadoRepository.findOne(estado.getId());
        updatedEstado
                .nombre(UPDATED_NOMBRE);

        restEstadoMockMvc.perform(put("/api/estados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEstado)))
            .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estadoList.get(estadoList.size() - 1);
        assertThat(testEstado.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingEstado() throws Exception {
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Create the Estado

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEstadoMockMvc.perform(put("/api/estados")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estado)))
            .andExpect(status().isCreated());

        // Validate the Estado in the database
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeDelete = estadoRepository.findAll().size();

        // Get the estado
        restEstadoMockMvc.perform(delete("/api/estados/{id}", estado.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Estado> estadoList = estadoRepository.findAll();
        assertThat(estadoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Estado.class);
    }
}

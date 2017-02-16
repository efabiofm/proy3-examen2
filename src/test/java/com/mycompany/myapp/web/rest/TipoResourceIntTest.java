package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Examen2App;

import com.mycompany.myapp.domain.Tipo;
import com.mycompany.myapp.repository.TipoRepository;
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
 * Test class for the TipoResource REST controller.
 *
 * @see TipoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Examen2App.class)
public class TipoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRECIO = 1D;
    private static final Double UPDATED_PRECIO = 2D;

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipoMockMvc;

    private Tipo tipo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            TipoResource tipoResource = new TipoResource(tipoRepository);
        this.restTipoMockMvc = MockMvcBuilders.standaloneSetup(tipoResource)
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
    public static Tipo createEntity(EntityManager em) {
        Tipo tipo = new Tipo()
                .nombre(DEFAULT_NOMBRE)
                .precio(DEFAULT_PRECIO);
        return tipo;
    }

    @Before
    public void initTest() {
        tipo = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipo() throws Exception {
        int databaseSizeBeforeCreate = tipoRepository.findAll().size();

        // Create the Tipo

        restTipoMockMvc.perform(post("/api/tipos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isCreated());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate + 1);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipo.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createTipoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoRepository.findAll().size();

        // Create the Tipo with an existing ID
        Tipo existingTipo = new Tipo();
        existingTipo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoMockMvc.perform(post("/api/tipos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTipo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTipos() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList
        restTipoMockMvc.perform(get("/api/tipos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);

        // Get the tipo
        restTipoMockMvc.perform(get("/api/tipos/{id}", tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTipo() throws Exception {
        // Get the tipo
        restTipoMockMvc.perform(get("/api/tipos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Update the tipo
        Tipo updatedTipo = tipoRepository.findOne(tipo.getId());
        updatedTipo
                .nombre(UPDATED_NOMBRE)
                .precio(UPDATED_PRECIO);

        restTipoMockMvc.perform(put("/api/tipos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipo)))
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate);
        Tipo testTipo = tipoList.get(tipoList.size() - 1);
        assertThat(testTipo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipo.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void updateNonExistingTipo() throws Exception {
        int databaseSizeBeforeUpdate = tipoRepository.findAll().size();

        // Create the Tipo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipoMockMvc.perform(put("/api/tipos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipo)))
            .andExpect(status().isCreated());

        // Validate the Tipo in the database
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipo() throws Exception {
        // Initialize the database
        tipoRepository.saveAndFlush(tipo);
        int databaseSizeBeforeDelete = tipoRepository.findAll().size();

        // Get the tipo
        restTipoMockMvc.perform(delete("/api/tipos/{id}", tipo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tipo> tipoList = tipoRepository.findAll();
        assertThat(tipoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tipo.class);
    }
}

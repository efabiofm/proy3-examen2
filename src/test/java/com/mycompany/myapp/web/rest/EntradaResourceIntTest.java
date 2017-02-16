package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Examen2App;

import com.mycompany.myapp.domain.Entrada;
import com.mycompany.myapp.repository.EntradaRepository;
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
 * Test class for the EntradaResource REST controller.
 *
 * @see EntradaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Examen2App.class)
public class EntradaResourceIntTest {

    private static final Double DEFAULT_KILOS = 1D;
    private static final Double UPDATED_KILOS = 2D;

    private static final ZonedDateTime DEFAULT_FECHA_ENTRADA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ENTRADA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntradaMockMvc;

    private Entrada entrada;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EntradaResource entradaResource = new EntradaResource(entradaRepository);
        this.restEntradaMockMvc = MockMvcBuilders.standaloneSetup(entradaResource)
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
    public static Entrada createEntity(EntityManager em) {
        Entrada entrada = new Entrada()
                .kilos(DEFAULT_KILOS)
                .fechaEntrada(DEFAULT_FECHA_ENTRADA);
        return entrada;
    }

    @Before
    public void initTest() {
        entrada = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntrada() throws Exception {
        int databaseSizeBeforeCreate = entradaRepository.findAll().size();

        // Create the Entrada

        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isCreated());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeCreate + 1);
        Entrada testEntrada = entradaList.get(entradaList.size() - 1);
        assertThat(testEntrada.getKilos()).isEqualTo(DEFAULT_KILOS);
        assertThat(testEntrada.getFechaEntrada()).isEqualTo(DEFAULT_FECHA_ENTRADA);
    }

    @Test
    @Transactional
    public void createEntradaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entradaRepository.findAll().size();

        // Create the Entrada with an existing ID
        Entrada existingEntrada = new Entrada();
        existingEntrada.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntradaMockMvc.perform(post("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEntrada)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntradas() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        // Get all the entradaList
        restEntradaMockMvc.perform(get("/api/entradas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entrada.getId().intValue())))
            .andExpect(jsonPath("$.[*].kilos").value(hasItem(DEFAULT_KILOS.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaEntrada").value(hasItem(sameInstant(DEFAULT_FECHA_ENTRADA))));
    }

    @Test
    @Transactional
    public void getEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);

        // Get the entrada
        restEntradaMockMvc.perform(get("/api/entradas/{id}", entrada.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entrada.getId().intValue()))
            .andExpect(jsonPath("$.kilos").value(DEFAULT_KILOS.doubleValue()))
            .andExpect(jsonPath("$.fechaEntrada").value(sameInstant(DEFAULT_FECHA_ENTRADA)));
    }

    @Test
    @Transactional
    public void getNonExistingEntrada() throws Exception {
        // Get the entrada
        restEntradaMockMvc.perform(get("/api/entradas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);
        int databaseSizeBeforeUpdate = entradaRepository.findAll().size();

        // Update the entrada
        Entrada updatedEntrada = entradaRepository.findOne(entrada.getId());
        updatedEntrada
                .kilos(UPDATED_KILOS)
                .fechaEntrada(UPDATED_FECHA_ENTRADA);

        restEntradaMockMvc.perform(put("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntrada)))
            .andExpect(status().isOk());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeUpdate);
        Entrada testEntrada = entradaList.get(entradaList.size() - 1);
        assertThat(testEntrada.getKilos()).isEqualTo(UPDATED_KILOS);
        assertThat(testEntrada.getFechaEntrada()).isEqualTo(UPDATED_FECHA_ENTRADA);
    }

    @Test
    @Transactional
    public void updateNonExistingEntrada() throws Exception {
        int databaseSizeBeforeUpdate = entradaRepository.findAll().size();

        // Create the Entrada

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntradaMockMvc.perform(put("/api/entradas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entrada)))
            .andExpect(status().isCreated());

        // Validate the Entrada in the database
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntrada() throws Exception {
        // Initialize the database
        entradaRepository.saveAndFlush(entrada);
        int databaseSizeBeforeDelete = entradaRepository.findAll().size();

        // Get the entrada
        restEntradaMockMvc.perform(delete("/api/entradas/{id}", entrada.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entrada> entradaList = entradaRepository.findAll();
        assertThat(entradaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entrada.class);
    }
}

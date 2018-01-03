package com.bierny.alert.web.rest;

import com.bierny.alert.AlertSystemApp;

import com.bierny.alert.domain.TestDab;
import com.bierny.alert.repository.TestDabRepository;
import com.bierny.alert.service.TestDabService;
import com.bierny.alert.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TestDabResource REST controller.
 *
 * @see TestDabResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AlertSystemApp.class)
public class TestDabResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SECOND = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SECOND = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TestDabRepository testDabRepository;

    @Autowired
    private TestDabService testDabService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestDabMockMvc;

    private TestDab testDab;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestDabResource testDabResource = new TestDabResource(testDabService);
        this.restTestDabMockMvc = MockMvcBuilders.standaloneSetup(testDabResource)
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
    public static TestDab createEntity(EntityManager em) {
        TestDab testDab = new TestDab()
            .name(DEFAULT_NAME)
            .second(DEFAULT_SECOND);
        return testDab;
    }

    @Before
    public void initTest() {
        testDab = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestDab() throws Exception {
        int databaseSizeBeforeCreate = testDabRepository.findAll().size();

        // Create the TestDab
        restTestDabMockMvc.perform(post("/api/test-dabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDab)))
            .andExpect(status().isCreated());

        // Validate the TestDab in the database
        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeCreate + 1);
        TestDab testTestDab = testDabList.get(testDabList.size() - 1);
        assertThat(testTestDab.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestDab.getSecond()).isEqualTo(DEFAULT_SECOND);
    }

    @Test
    @Transactional
    public void createTestDabWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testDabRepository.findAll().size();

        // Create the TestDab with an existing ID
        testDab.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestDabMockMvc.perform(post("/api/test-dabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDab)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testDabRepository.findAll().size();
        // set the field null
        testDab.setName(null);

        // Create the TestDab, which fails.

        restTestDabMockMvc.perform(post("/api/test-dabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDab)))
            .andExpect(status().isBadRequest());

        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestDabs() throws Exception {
        // Initialize the database
        testDabRepository.saveAndFlush(testDab);

        // Get all the testDabList
        restTestDabMockMvc.perform(get("/api/test-dabs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testDab.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].second").value(hasItem(DEFAULT_SECOND.toString())));
    }

    @Test
    @Transactional
    public void getTestDab() throws Exception {
        // Initialize the database
        testDabRepository.saveAndFlush(testDab);

        // Get the testDab
        restTestDabMockMvc.perform(get("/api/test-dabs/{id}", testDab.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testDab.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.second").value(DEFAULT_SECOND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestDab() throws Exception {
        // Get the testDab
        restTestDabMockMvc.perform(get("/api/test-dabs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestDab() throws Exception {
        // Initialize the database
        testDabService.save(testDab);

        int databaseSizeBeforeUpdate = testDabRepository.findAll().size();

        // Update the testDab
        TestDab updatedTestDab = testDabRepository.findOne(testDab.getId());
        updatedTestDab
            .name(UPDATED_NAME)
            .second(UPDATED_SECOND);

        restTestDabMockMvc.perform(put("/api/test-dabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTestDab)))
            .andExpect(status().isOk());

        // Validate the TestDab in the database
        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeUpdate);
        TestDab testTestDab = testDabList.get(testDabList.size() - 1);
        assertThat(testTestDab.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestDab.getSecond()).isEqualTo(UPDATED_SECOND);
    }

    @Test
    @Transactional
    public void updateNonExistingTestDab() throws Exception {
        int databaseSizeBeforeUpdate = testDabRepository.findAll().size();

        // Create the TestDab

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestDabMockMvc.perform(put("/api/test-dabs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDab)))
            .andExpect(status().isCreated());

        // Validate the TestDab in the database
        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestDab() throws Exception {
        // Initialize the database
        testDabService.save(testDab);

        int databaseSizeBeforeDelete = testDabRepository.findAll().size();

        // Get the testDab
        restTestDabMockMvc.perform(delete("/api/test-dabs/{id}", testDab.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TestDab> testDabList = testDabRepository.findAll();
        assertThat(testDabList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestDab.class);
        TestDab testDab1 = new TestDab();
        testDab1.setId(1L);
        TestDab testDab2 = new TestDab();
        testDab2.setId(testDab1.getId());
        assertThat(testDab1).isEqualTo(testDab2);
        testDab2.setId(2L);
        assertThat(testDab1).isNotEqualTo(testDab2);
        testDab1.setId(null);
        assertThat(testDab1).isNotEqualTo(testDab2);
    }
}

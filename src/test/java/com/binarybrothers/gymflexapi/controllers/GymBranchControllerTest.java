package com.binarybrothers.gymflexapi.controllers;

import static com.binarybrothers.gymflexapi.controllers.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.entities.GymBranch;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.repositories.GymBranchRepository;
import com.binarybrothers.gymflexapi.services.auth.AuthenticationService;
import com.binarybrothers.gymflexapi.services.mappers.GymBranchMapper;
import com.binarybrothers.gymflexapi.utils.auth.AuthenticationResponse;
import com.binarybrothers.gymflexapi.utils.auth.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GymBranchController} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class GymBranchControllerTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "6SK@C4\\\\%PKR";
    private static final String UPDATED_EMAIL = "ZD7@H1Q-.\\\\|JRSMD";

    private static final String DEFAULT_APP_PASSWORD_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_APP_PASSWORD_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_OPENING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_OPENING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CLOSING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CLOSING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_SESSION_DURATION_ALLOWED = 1F;
    private static final Float UPDATED_SESSION_DURATION_ALLOWED = 2F;

    private static final String ENTITY_API_URL = "/api/gym-branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GymBranchRepository gymBranchRepository;

    @Autowired
    private GymBranchMapper gymBranchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGymBranchMockMvc;

    private GymBranch gymBranch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GymBranch createEntity(EntityManager em) {
        GymBranch gymBranch = new GymBranch()
            .name(DEFAULT_NAME)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .adress(DEFAULT_ADRESS)
            .email(DEFAULT_EMAIL)
            .appPasswordEmail(DEFAULT_APP_PASSWORD_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .openingDate(DEFAULT_OPENING_DATE)
            .closingDate(DEFAULT_CLOSING_DATE)
            .sessionDurationAllowed(DEFAULT_SESSION_DURATION_ALLOWED);
        return gymBranch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GymBranch createUpdatedEntity(EntityManager em) {
        GymBranch gymBranch = new GymBranch()
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .appPasswordEmail(UPDATED_APP_PASSWORD_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE)
            .sessionDurationAllowed(UPDATED_SESSION_DURATION_ALLOWED);
        return gymBranch;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        gymBranch = createEntity(em);
        RegisterRequest request = new RegisterRequest("testFirstName","testLastName","testLogin","test@gmail.com","testPassword");

        Role roleUser = Role.builder()
                .name("ClientVisiter")
                .description("For client that visit our site and sign up")
                .inventory(true)
                .build();
        AuthenticationResponse authenticationResponse = authenticationService.register(request,roleUser);
        token = authenticationResponse.getAccessToken();
    }

    @Test
    @Transactional
    void createGymBranch() throws Exception {
        int databaseSizeBeforeCreate = gymBranchRepository.findAll().size();
        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);
        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isCreated());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeCreate + 1);
        GymBranch testGymBranch = gymBranchList.get(gymBranchList.size() - 1);
        assertThat(testGymBranch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGymBranch.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testGymBranch.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testGymBranch.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testGymBranch.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGymBranch.getAppPasswordEmail()).isEqualTo(DEFAULT_APP_PASSWORD_EMAIL);
        assertThat(testGymBranch.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testGymBranch.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testGymBranch.getClosingDate()).isEqualTo(DEFAULT_CLOSING_DATE);
        assertThat(testGymBranch.getSessionDurationAllowed()).isEqualTo(DEFAULT_SESSION_DURATION_ALLOWED);
    }

    @Test
    @Transactional
    void createGymBranchWithExistingId() throws Exception {
        // Create the GymBranch with an existing ID
        gymBranch.setId(1L);
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        int databaseSizeBeforeCreate = gymBranchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gymBranchRepository.findAll().size();
        // set the field null
        gymBranch.setName(null);

        // Create the GymBranch, which fails.
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = gymBranchRepository.findAll().size();
        // set the field null
        gymBranch.setEmail(null);

        // Create the GymBranch, which fails.
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpeningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gymBranchRepository.findAll().size();
        // set the field null
        gymBranch.setOpeningDate(null);

        // Create the GymBranch, which fails.
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClosingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gymBranchRepository.findAll().size();
        // set the field null
        gymBranch.setClosingDate(null);

        // Create the GymBranch, which fails.
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionDurationAllowedIsRequired() throws Exception {
        int databaseSizeBeforeTest = gymBranchRepository.findAll().size();
        // set the field null
        gymBranch.setSessionDurationAllowed(null);

        // Create the GymBranch, which fails.
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        restGymBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isBadRequest());

        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGymBranches() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        // Get all the gymBranchList
        restGymBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gymBranch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].appPasswordEmail").value(hasItem(DEFAULT_APP_PASSWORD_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(sameInstant(DEFAULT_OPENING_DATE))))
            .andExpect(jsonPath("$.[*].closingDate").value(hasItem(sameInstant(DEFAULT_CLOSING_DATE))))
            .andExpect(jsonPath("$.[*].sessionDurationAllowed").value(hasItem(DEFAULT_SESSION_DURATION_ALLOWED.doubleValue())));
    }

    @Test
    @Transactional
    void getGymBranch() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        // Get the gymBranch
        restGymBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, gymBranch.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gymBranch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.appPasswordEmail").value(DEFAULT_APP_PASSWORD_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.openingDate").value(sameInstant(DEFAULT_OPENING_DATE)))
            .andExpect(jsonPath("$.closingDate").value(sameInstant(DEFAULT_CLOSING_DATE)))
            .andExpect(jsonPath("$.sessionDurationAllowed").value(DEFAULT_SESSION_DURATION_ALLOWED.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingGymBranch() throws Exception {
        // Get the gymBranch
        restGymBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGymBranch() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();

        // Update the gymBranch
        GymBranch updatedGymBranch = gymBranchRepository.findById(gymBranch.getId()).get();
        // Disconnect from session so that the updates on updatedGymBranch are not directly saved in db
        em.detach(updatedGymBranch);
        updatedGymBranch
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .appPasswordEmail(UPDATED_APP_PASSWORD_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE)
            .sessionDurationAllowed(UPDATED_SESSION_DURATION_ALLOWED);
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(updatedGymBranch);

        restGymBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gymBranchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isOk());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
        GymBranch testGymBranch = gymBranchList.get(gymBranchList.size() - 1);
        assertThat(testGymBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGymBranch.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGymBranch.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testGymBranch.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testGymBranch.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGymBranch.getAppPasswordEmail()).isEqualTo(UPDATED_APP_PASSWORD_EMAIL);
        assertThat(testGymBranch.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGymBranch.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testGymBranch.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
        assertThat(testGymBranch.getSessionDurationAllowed()).isEqualTo(UPDATED_SESSION_DURATION_ALLOWED);
    }

    @Test
    @Transactional
    void putNonExistingGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gymBranchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGymBranchWithPatch() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();

        // Update the gymBranch using partial update
        GymBranch partialUpdatedGymBranch = new GymBranch();
        partialUpdatedGymBranch.setId(gymBranch.getId());

        partialUpdatedGymBranch
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE);

        restGymBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGymBranch.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGymBranch))
            )
            .andExpect(status().isOk());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
        GymBranch testGymBranch = gymBranchList.get(gymBranchList.size() - 1);
        assertThat(testGymBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGymBranch.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGymBranch.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testGymBranch.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testGymBranch.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGymBranch.getAppPasswordEmail()).isEqualTo(DEFAULT_APP_PASSWORD_EMAIL);
        assertThat(testGymBranch.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGymBranch.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testGymBranch.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
        assertThat(testGymBranch.getSessionDurationAllowed()).isEqualTo(DEFAULT_SESSION_DURATION_ALLOWED);
    }

    @Test
    @Transactional
    void fullUpdateGymBranchWithPatch() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();

        // Update the gymBranch using partial update
        GymBranch partialUpdatedGymBranch = new GymBranch();
        partialUpdatedGymBranch.setId(gymBranch.getId());

        partialUpdatedGymBranch
            .name(UPDATED_NAME)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adress(UPDATED_ADRESS)
            .email(UPDATED_EMAIL)
            .appPasswordEmail(UPDATED_APP_PASSWORD_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE)
            .sessionDurationAllowed(UPDATED_SESSION_DURATION_ALLOWED);

        restGymBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGymBranch.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGymBranch))
            )
            .andExpect(status().isOk());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
        GymBranch testGymBranch = gymBranchList.get(gymBranchList.size() - 1);
        assertThat(testGymBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGymBranch.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGymBranch.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testGymBranch.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testGymBranch.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGymBranch.getAppPasswordEmail()).isEqualTo(UPDATED_APP_PASSWORD_EMAIL);
        assertThat(testGymBranch.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testGymBranch.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testGymBranch.getClosingDate()).isEqualTo(UPDATED_CLOSING_DATE);
        assertThat(testGymBranch.getSessionDurationAllowed()).isEqualTo(UPDATED_SESSION_DURATION_ALLOWED);
    }

    @Test
    @Transactional
    void patchNonExistingGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gymBranchDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGymBranch() throws Exception {
        int databaseSizeBeforeUpdate = gymBranchRepository.findAll().size();
        gymBranch.setId(count.incrementAndGet());

        // Create the GymBranch
        GymBranchDTO gymBranchDTO = gymBranchMapper.toDto(gymBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymBranchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(gymBranchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GymBranch in the database
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGymBranch() throws Exception {
        // Initialize the database
        gymBranchRepository.saveAndFlush(gymBranch);

        int databaseSizeBeforeDelete = gymBranchRepository.findAll().size();

        // Delete the gymBranch
        restGymBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, gymBranch.getId()).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GymBranch> gymBranchList = gymBranchRepository.findAll();
        assertThat(gymBranchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

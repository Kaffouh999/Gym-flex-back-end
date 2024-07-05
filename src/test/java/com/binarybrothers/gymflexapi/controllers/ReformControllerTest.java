package com.binarybrothers.gymflexapi.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.binarybrothers.gymflexapi.dtos.reform.ReformDTO;
import com.binarybrothers.gymflexapi.entities.EquipmentItem;
import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.entities.Reform;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.repositories.ReformRepository;
import com.binarybrothers.gymflexapi.services.auth.AuthenticationService;
import com.binarybrothers.gymflexapi.services.mappers.ReformMapper;
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


@SpringBootTest
@AutoConfigureMockMvc
class ReformControllerTest {

    private static final ZonedDateTime DEFAULT_DECISION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DECISION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final LocalDateTime RECEIVED_UPDATED_DECISION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0).toLocalDateTime();

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reforms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReformRepository reformRepository;

    @Autowired
    private ReformMapper reformMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReformMockMvc;

    private Reform reform;
    private String expectedDate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reform createEntity(EntityManager em) {
        Reform reform = new Reform().decisionDate(DEFAULT_DECISION_DATE).comment(DEFAULT_COMMENT);
        // Add required entity
        EquipmentItem equipmentItem;
        if (TestUtil.findAll(em, EquipmentItem.class).isEmpty()) {
            equipmentItem = EquipmentItemControllerTest.createEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        reform.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberControllerTest.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        reform.setDecider(member);
        return reform;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reform createUpdatedEntity(EntityManager em) {
        Reform reform = new Reform().decisionDate(UPDATED_DECISION_DATE).comment(UPDATED_COMMENT);
        // Add required entity
        EquipmentItem equipmentItem;
        if (TestUtil.findAll(em, EquipmentItem.class).isEmpty()) {
            equipmentItem = EquipmentItemControllerTest.createUpdatedEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        reform.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberControllerTest.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        reform.setDecider(member);
        return reform;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        reform = createEntity(em);
        RegisterRequest request = new RegisterRequest("testFirstName","testLastName","testLogin","test@gmail.com","testPassword");

        Role roleUser = Role.builder()
                .name("ClientVisiter")
                .description("For client that visit our site and sign up")
                .inventory(true)
                .build();
        AuthenticationResponse authenticationResponse = authenticationService.register(request,roleUser);
        token=authenticationResponse.getAccessToken();

        LocalDateTime RECEIVED_DEFAULT_DECISION_DATE = DEFAULT_DECISION_DATE.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        expectedDate = RECEIVED_DEFAULT_DECISION_DATE.format(formatter);
    }

    @Test
    @Transactional
    void createReform() throws Exception {
        int databaseSizeBeforeCreate = reformRepository.findAll().size();
        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);
        restReformMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
            .andExpect(status().isCreated());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeCreate + 1);
        Reform testReform = reformList.get(reformList.size() - 1);
        assertThat(testReform.getDecisionDate()).isEqualTo(DEFAULT_DECISION_DATE);
        assertThat(testReform.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createReformWithExistingId() throws Exception {
        // Create the Reform with an existing ID
        reform.setId(1L);
        ReformDTO reformDTO = reformMapper.toDto(reform);

        int databaseSizeBeforeCreate = reformRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReformMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReforms() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        // Get all the reformList
        restReformMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reform.getId().intValue())))
            .andExpect(jsonPath("$.[*].decisionDate").value(hasItem(expectedDate)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getReform() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        // Get the reform
        restReformMockMvc
            .perform(get(ENTITY_API_URL_ID, reform.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reform.getId().intValue()))
            .andExpect(jsonPath("$.decisionDate").value(expectedDate))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
                .andDo(print());
    }

    @Test
    @Transactional
    void getNonExistingReform() throws Exception {
        // Get the reform
        restReformMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReform() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        int databaseSizeBeforeUpdate = reformRepository.findAll().size();

        // Update the reform
        Reform updatedReform = reformRepository.findById(reform.getId()).get();
        // Disconnect from session so that the updates on updatedReform are not directly saved in db
        em.detach(updatedReform);
        updatedReform.decisionDate(UPDATED_DECISION_DATE).comment(UPDATED_COMMENT);
        ReformDTO reformDTO = reformMapper.toDto(updatedReform);

        restReformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reformDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
        Reform testReform = reformList.get(reformList.size() - 1);
        assertThat(testReform.getDecisionDate()).isEqualTo(UPDATED_DECISION_DATE);
        assertThat(testReform.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reformDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReformWithPatch() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        int databaseSizeBeforeUpdate = reformRepository.findAll().size();

        // Update the reform using partial update
        Reform partialUpdatedReform = new Reform();
        partialUpdatedReform.setId(reform.getId());

        partialUpdatedReform.decisionDate(UPDATED_DECISION_DATE).comment(UPDATED_COMMENT);

        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReform.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReform))
            )
            .andExpect(status().isOk());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
        Reform testReform = reformList.get(reformList.size() - 1);
        assertThat(testReform.getDecisionDate()).isEqualTo(UPDATED_DECISION_DATE);
        assertThat(testReform.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateReformWithPatch() throws Exception {
        // Initialize the database
        Reform newReform = reformRepository.saveAndFlush(reform);

        int databaseSizeBeforeUpdate = reformRepository.findAll().size();

        // Update the reform using partial update
        Reform partialUpdatedReform = new Reform();
        partialUpdatedReform.setId(newReform.getId());

        partialUpdatedReform.decisionDate(UPDATED_DECISION_DATE).comment(UPDATED_COMMENT);

        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReform.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReform))
            )
            .andExpect(status().isOk());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
        Reform testReform = reformList.get(reformList.size() - 1);
        assertThat(testReform.getDecisionDate()).isEqualTo(UPDATED_DECISION_DATE);
        assertThat(testReform.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reformDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReform() throws Exception {
        int databaseSizeBeforeUpdate = reformRepository.findAll().size();
        reform.setId(count.incrementAndGet());

        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(reformDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reform in the database
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReform() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        int databaseSizeBeforeDelete = reformRepository.findAll().size();

        // Delete the reform
        restReformMockMvc
            .perform(delete(ENTITY_API_URL_ID, reform.getId()).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

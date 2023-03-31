package com.example.GymInTheBack.web;


import static com.example.GymInTheBack.web.TestUtil.sameInstant;
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

import com.example.GymInTheBack.dtos.reform.ReformDTO;
import com.example.GymInTheBack.entities.EquipmentItem;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Reform;
import com.example.GymInTheBack.repositories.ReformRepository;
import com.example.GymInTheBack.services.mappers.ReformMapper;
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
class ReformResourceTest {

    private static final ZonedDateTime DEFAULT_DECISION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DECISION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reforms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
            equipmentItem = EquipmentItemResourceTest.createEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        reform.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createEntity(em);
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
            equipmentItem = EquipmentItemResourceTest.createUpdatedEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        reform.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        reform.setDecider(member);
        return reform;
    }

    @BeforeEach
    public void initTest() {
        reform = createEntity(em);
    }

    @Test
    @Transactional
    void createReform() throws Exception {
        int databaseSizeBeforeCreate = reformRepository.findAll().size();
        // Create the Reform
        ReformDTO reformDTO = reformMapper.toDto(reform);
        restReformMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
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
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
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
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reform.getId().intValue())))
            .andExpect(jsonPath("$.[*].decisionDate").value(hasItem(sameInstant(DEFAULT_DECISION_DATE))))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getReform() throws Exception {
        // Initialize the database
        reformRepository.saveAndFlush(reform);

        // Get the reform
        restReformMockMvc
            .perform(get(ENTITY_API_URL_ID, reform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reform.getId().intValue()))
            .andExpect(jsonPath("$.decisionDate").value(sameInstant(DEFAULT_DECISION_DATE)))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingReform() throws Exception {
        // Get the reform
        restReformMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
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
                    .contentType(MediaType.APPLICATION_JSON)
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
                    .contentType(MediaType.APPLICATION_JSON)
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
                    .contentType(MediaType.APPLICATION_JSON)
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
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reformDTO)))
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
                    .contentType("application/merge-patch+json")
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
        reformRepository.saveAndFlush(reform);

        int databaseSizeBeforeUpdate = reformRepository.findAll().size();

        // Update the reform using partial update
        Reform partialUpdatedReform = new Reform();
        partialUpdatedReform.setId(reform.getId());

        partialUpdatedReform.decisionDate(UPDATED_DECISION_DATE).comment(UPDATED_COMMENT);

        restReformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReform.getId())
                    .contentType("application/merge-patch+json")
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
                    .contentType("application/merge-patch+json")
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
                    .contentType("application/merge-patch+json")
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
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reformDTO))
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
            .perform(delete(ENTITY_API_URL_ID, reform.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reform> reformList = reformRepository.findAll();
        assertThat(reformList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

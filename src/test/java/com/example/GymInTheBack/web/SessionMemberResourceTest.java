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

import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.SessionMember;
import com.example.GymInTheBack.repositories.SessionMemberRepository;
import com.example.GymInTheBack.services.mappers.SessionMemberMapper;
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
 * Integration tests for the {@link SessionMemberResource} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SessionMemberResourceTest {

    private static final ZonedDateTime DEFAULT_ENTERING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENTERING_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LEAVING_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LEAVING_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/session-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionMemberRepository sessionMemberRepository;

    @Autowired
    private SessionMemberMapper sessionMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionMemberMockMvc;

    private SessionMember sessionMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionMember createEntity(EntityManager em) {
        SessionMember sessionMember = new SessionMember().enteringTime(DEFAULT_ENTERING_TIME).leavingTime(DEFAULT_LEAVING_TIME);
        // Add required entity
        GymBranch gymBranch;
        if (TestUtil.findAll(em, GymBranch.class).isEmpty()) {
            gymBranch = GymBranchResourceTest.createEntity(em);
            em.persist(gymBranch);
            em.flush();
        } else {
            gymBranch = TestUtil.findAll(em, GymBranch.class).get(0);
        }
        sessionMember.setGymBranch(gymBranch);
        return sessionMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionMember createUpdatedEntity(EntityManager em) {
        SessionMember sessionMember = new SessionMember().enteringTime(UPDATED_ENTERING_TIME).leavingTime(UPDATED_LEAVING_TIME);
        // Add required entity
        GymBranch gymBranch;
        if (TestUtil.findAll(em, GymBranch.class).isEmpty()) {
            gymBranch = GymBranchResourceTest.createUpdatedEntity(em);
            em.persist(gymBranch);
            em.flush();
        } else {
            gymBranch = TestUtil.findAll(em, GymBranch.class).get(0);
        }
        sessionMember.setGymBranch(gymBranch);
        return sessionMember;
    }

    @BeforeEach
    public void initTest() {
        sessionMember = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionMember() throws Exception {
        int databaseSizeBeforeCreate = sessionMemberRepository.findAll().size();
        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);
        restSessionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeCreate + 1);
        SessionMember testSessionMember = sessionMemberList.get(sessionMemberList.size() - 1);
        assertThat(testSessionMember.getEnteringTime()).isEqualTo(DEFAULT_ENTERING_TIME);
        assertThat(testSessionMember.getLeavingTime()).isEqualTo(DEFAULT_LEAVING_TIME);
    }

    @Test
    @Transactional
    void createSessionMemberWithExistingId() throws Exception {
        // Create the SessionMember with an existing ID
        sessionMember.setId(1L);
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        int databaseSizeBeforeCreate = sessionMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnteringTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionMemberRepository.findAll().size();
        // set the field null
        sessionMember.setEnteringTime(null);

        // Create the SessionMember, which fails.
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        restSessionMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSessionMembers() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        // Get all the sessionMemberList
        restSessionMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].enteringTime").value(hasItem(sameInstant(DEFAULT_ENTERING_TIME))))
            .andExpect(jsonPath("$.[*].leavingTime").value(hasItem(sameInstant(DEFAULT_LEAVING_TIME))));
    }

    @Test
    @Transactional
    void getSessionMember() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        // Get the sessionMember
        restSessionMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionMember.getId().intValue()))
            .andExpect(jsonPath("$.enteringTime").value(sameInstant(DEFAULT_ENTERING_TIME)))
            .andExpect(jsonPath("$.leavingTime").value(sameInstant(DEFAULT_LEAVING_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingSessionMember() throws Exception {
        // Get the sessionMember
        restSessionMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionMember() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();

        // Update the sessionMember
        SessionMember updatedSessionMember = sessionMemberRepository.findById(sessionMember.getId()).get();
        // Disconnect from session so that the updates on updatedSessionMember are not directly saved in db
        em.detach(updatedSessionMember);
        updatedSessionMember.enteringTime(UPDATED_ENTERING_TIME).leavingTime(UPDATED_LEAVING_TIME);
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(updatedSessionMember);

        restSessionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
        SessionMember testSessionMember = sessionMemberList.get(sessionMemberList.size() - 1);
        assertThat(testSessionMember.getEnteringTime()).isEqualTo(UPDATED_ENTERING_TIME);
        assertThat(testSessionMember.getLeavingTime()).isEqualTo(UPDATED_LEAVING_TIME);
    }

    @Test
    @Transactional
    void putNonExistingSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionMemberWithPatch() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();

        // Update the sessionMember using partial update
        SessionMember partialUpdatedSessionMember = new SessionMember();
        partialUpdatedSessionMember.setId(sessionMember.getId());

        partialUpdatedSessionMember.enteringTime(UPDATED_ENTERING_TIME);

        restSessionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionMember))
            )
            .andExpect(status().isOk());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
        SessionMember testSessionMember = sessionMemberList.get(sessionMemberList.size() - 1);
        assertThat(testSessionMember.getEnteringTime()).isEqualTo(UPDATED_ENTERING_TIME);
        assertThat(testSessionMember.getLeavingTime()).isEqualTo(DEFAULT_LEAVING_TIME);
    }

    @Test
    @Transactional
    void fullUpdateSessionMemberWithPatch() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();

        // Update the sessionMember using partial update
        SessionMember partialUpdatedSessionMember = new SessionMember();
        partialUpdatedSessionMember.setId(sessionMember.getId());

        partialUpdatedSessionMember.enteringTime(UPDATED_ENTERING_TIME).leavingTime(UPDATED_LEAVING_TIME);

        restSessionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionMember))
            )
            .andExpect(status().isOk());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
        SessionMember testSessionMember = sessionMemberList.get(sessionMemberList.size() - 1);
        assertThat(testSessionMember.getEnteringTime()).isEqualTo(UPDATED_ENTERING_TIME);
        assertThat(testSessionMember.getLeavingTime()).isEqualTo(UPDATED_LEAVING_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionMember() throws Exception {
        int databaseSizeBeforeUpdate = sessionMemberRepository.findAll().size();
        sessionMember.setId(count.incrementAndGet());

        // Create the SessionMember
        SessionMemberDTO sessionMemberDTO = sessionMemberMapper.toDto(sessionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionMember in the database
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionMember() throws Exception {
        // Initialize the database
        sessionMemberRepository.saveAndFlush(sessionMember);

        int databaseSizeBeforeDelete = sessionMemberRepository.findAll().size();

        // Delete the sessionMember
        restSessionMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionMember> sessionMemberList = sessionMemberRepository.findAll();
        assertThat(sessionMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

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

import com.example.GymInTheBack.dtos.maintining.MaintiningDTO;
import com.example.GymInTheBack.entities.*;
import com.example.GymInTheBack.repositories.MaintiningRepository;
import com.example.GymInTheBack.services.auth.AuthenticationService;
import com.example.GymInTheBack.services.auth.JwtService;
import com.example.GymInTheBack.services.mappers.MaintiningMapper;
import com.example.GymInTheBack.utils.auth.AuthenticationRequest;
import com.example.GymInTheBack.utils.auth.AuthenticationResponse;
import com.example.GymInTheBack.utils.auth.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
class MaintiningResourceTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final String ENTITY_API_URL = "/api/maintinings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static String token="";
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaintiningRepository maintiningRepository;

    @Autowired
    private MaintiningMapper maintiningMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintiningMockMvc;



    @Autowired
    private AuthenticationService authenticationService;

    private Maintining maintining;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintining createEntity(EntityManager em) {
        Maintining maintining = new Maintining().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).cost(DEFAULT_COST);
        // Add required entity
        EquipmentItem equipmentItem;
        if (TestUtil.findAll(em, EquipmentItem.class).isEmpty()) {
            equipmentItem = EquipmentItemResourceTest.createEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        maintining.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        maintining.setMaintainerResponsible(member);
        return maintining;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintining createUpdatedEntity(EntityManager em) {
        Maintining maintining = new Maintining().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).cost(UPDATED_COST);
        // Add required entity
        EquipmentItem equipmentItem;
        if (TestUtil.findAll(em, EquipmentItem.class).isEmpty()) {
            equipmentItem = EquipmentItemResourceTest.createUpdatedEntity(em);
            em.persist(equipmentItem);
            em.flush();
        } else {
            equipmentItem = TestUtil.findAll(em, EquipmentItem.class).get(0);
        }
        maintining.setItem(equipmentItem);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        maintining.setMaintainerResponsible(member);
        return maintining;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        maintining = createEntity(em);
        RegisterRequest request = new RegisterRequest("testFirstName","testLastName","testLogin","test@gmail.com","testPassword");

        Role roleUser = Role.builder()
                .name("ClientVisiter")
                .description("For client that visit our site and sign up")
                .inventory(true)
                .build();
        AuthenticationResponse authenticationResponse = authenticationService.register(request,roleUser);
        MaintiningResourceTest.token = authenticationResponse.getAccessToken();
    }

    @Test
    @Transactional
    void createMaintining() throws Exception {
        int databaseSizeBeforeCreate = maintiningRepository.findAll().size();
        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);
        restMaintiningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(maintiningDTO)))
            .andExpect(status().isCreated());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeCreate + 1);
        Maintining testMaintining = maintiningList.get(maintiningList.size() - 1);
        assertThat(testMaintining.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMaintining.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMaintining.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createMaintiningWithExistingId() throws Exception {
        // Create the Maintining with an existing ID
        maintining.setId(1L);
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        int databaseSizeBeforeCreate = maintiningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintiningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(maintiningDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = maintiningRepository.findAll().size();
        // set the field null
        maintining.setStartDate(null);

        // Create the Maintining, which fails.
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        restMaintiningMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(maintiningDTO)))
            .andExpect(status().isBadRequest());

        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaintinings() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        // Get all the maintiningList
        restMaintiningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintining.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }

    @Test
    @Transactional
    void getMaintining() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        // Get the maintining
        restMaintiningMockMvc
            .perform(get(ENTITY_API_URL_ID, maintining.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintining.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaintining() throws Exception {
        // Get the maintining
        restMaintiningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintining() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();

        // Update the maintining
        Maintining updatedMaintining = maintiningRepository.findById(maintining.getId()).get();
        // Disconnect from session so that the updates on updatedMaintining are not directly saved in db
        em.detach(updatedMaintining);
        updatedMaintining.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).cost(UPDATED_COST);
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(updatedMaintining);

        restMaintiningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintiningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
        Maintining testMaintining = maintiningList.get(maintiningList.size() - 1);
        assertThat(testMaintining.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMaintining.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMaintining.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintiningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(maintiningDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintiningWithPatch() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();

        // Update the maintining using partial update
        Maintining partialUpdatedMaintining = new Maintining();
        partialUpdatedMaintining.setId(maintining.getId());

        partialUpdatedMaintining.startDate(UPDATED_START_DATE);

        restMaintiningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintining.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintining))
            )
            .andExpect(status().isOk());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
        Maintining testMaintining = maintiningList.get(maintiningList.size() - 1);
        assertThat(testMaintining.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMaintining.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMaintining.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void fullUpdateMaintiningWithPatch() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();

        // Update the maintining using partial update
        Maintining partialUpdatedMaintining = new Maintining();
        partialUpdatedMaintining.setId(maintining.getId());

        partialUpdatedMaintining.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).cost(UPDATED_COST);

        restMaintiningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintining.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintining))
            )
            .andExpect(status().isOk());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
        Maintining testMaintining = maintiningList.get(maintiningList.size() - 1);
        assertThat(testMaintining.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMaintining.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMaintining.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintiningDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintining() throws Exception {
        int databaseSizeBeforeUpdate = maintiningRepository.findAll().size();
        maintining.setId(count.incrementAndGet());

        // Create the Maintining
        MaintiningDTO maintiningDTO = maintiningMapper.toDto(maintining);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintiningMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(maintiningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintining in the database
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintining() throws Exception {
        // Initialize the database
        maintiningRepository.saveAndFlush(maintining);

        int databaseSizeBeforeDelete = maintiningRepository.findAll().size();

        // Delete the maintining
        restMaintiningMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintining.getId()).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maintining> maintiningList = maintiningRepository.findAll();
        assertThat(maintiningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

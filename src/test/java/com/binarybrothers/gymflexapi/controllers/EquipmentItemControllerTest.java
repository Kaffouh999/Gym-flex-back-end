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

import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.entities.EquipmentItem;
import com.binarybrothers.gymflexapi.entities.GymBranch;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.repositories.EquipmentItemRepository;
import com.binarybrothers.gymflexapi.services.auth.AuthenticationService;
import com.binarybrothers.gymflexapi.services.mappers.EquipmentItemMapper;
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
class EquipmentItemControllerTest {

    private static final ZonedDateTime DEFAULT_FIRST_USE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FIRST_USE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_AMORTIZATION = 1D;
    private static final Double UPDATED_AMORTIZATION = 2D;

    private static final String DEFAULT_BARE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BARE_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_SAFE_MIN_VALUE = 1D;
    private static final Double UPDATED_SAFE_MIN_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/equipment-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private EquipmentItemMapper equipmentItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentItemMockMvc;

    private EquipmentItem equipmentItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentItem createEntity(EntityManager em) {
        EquipmentItem equipmentItem = new EquipmentItem()
            .firstUseDate(DEFAULT_FIRST_USE_DATE)
            .price(DEFAULT_PRICE)
            .amortization(DEFAULT_AMORTIZATION)
            .bareCode(DEFAULT_BARE_CODE)
            .safeMinValue(DEFAULT_SAFE_MIN_VALUE);
        // Add required entity
        Equipment equipment;
        if (TestUtil.findAll(em, Equipment.class).isEmpty()) {
            equipment = EquipmentControllerTest.createEntity(em);
            em.persist(equipment);
            em.flush();
        } else {
            equipment = TestUtil.findAll(em, Equipment.class).get(0);
        }
        equipmentItem.setEquipment(equipment);
        // Add required entity
        GymBranch gymBranch;
        if (TestUtil.findAll(em, GymBranch.class).isEmpty()) {
            gymBranch = GymBranchControllerTest.createEntity(em);
            em.persist(gymBranch);
            em.flush();
        } else {
            gymBranch = TestUtil.findAll(em, GymBranch.class).get(0);
        }
        equipmentItem.setGymBranch(gymBranch);
        return equipmentItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentItem createUpdatedEntity(EntityManager em) {
        EquipmentItem equipmentItem = new EquipmentItem()
            .firstUseDate(UPDATED_FIRST_USE_DATE)
            .price(UPDATED_PRICE)
            .amortization(UPDATED_AMORTIZATION)
            .bareCode(UPDATED_BARE_CODE)
            .safeMinValue(UPDATED_SAFE_MIN_VALUE);
        // Add required entity
        Equipment equipment;
        if (TestUtil.findAll(em, Equipment.class).isEmpty()) {
            equipment = EquipmentControllerTest.createUpdatedEntity(em);
            em.persist(equipment);
            em.flush();
        } else {
            equipment = TestUtil.findAll(em, Equipment.class).get(0);
        }
        equipmentItem.setEquipment(equipment);
        // Add required entity
        GymBranch gymBranch;
        if (TestUtil.findAll(em, GymBranch.class).isEmpty()) {
            gymBranch = GymBranchControllerTest.createUpdatedEntity(em);
            em.persist(gymBranch);
            em.flush();
        } else {
            gymBranch = TestUtil.findAll(em, GymBranch.class).get(0);
        }
        equipmentItem.setGymBranch(gymBranch);
        return equipmentItem;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        equipmentItem = createEntity(em);
        RegisterRequest request = new RegisterRequest("testFirstName","testLastName","testLogin","test@gmail.com","testPassword");

        Role roleUser = Role.builder()
                .name("ClientVisiter")
                .description("For client that visit our site and sign up")
                .inventory(true)
                .build();
        AuthenticationResponse authenticationResponse = authenticationService.register(request,roleUser);
        token=authenticationResponse.getAccessToken();
    }

    @Test
    @Transactional
    void createEquipmentItem() throws Exception {
        int databaseSizeBeforeCreate = equipmentItemRepository.findAll().size();
        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);
        restEquipmentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentItem testEquipmentItem = equipmentItemList.get(equipmentItemList.size() - 1);
        assertThat(testEquipmentItem.getFirstUseDate()).isEqualTo(DEFAULT_FIRST_USE_DATE);
        assertThat(testEquipmentItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testEquipmentItem.getAmortization()).isEqualTo(DEFAULT_AMORTIZATION);
        assertThat(testEquipmentItem.getBareCode()).isEqualTo(DEFAULT_BARE_CODE);
        assertThat(testEquipmentItem.getSafeMinValue()).isEqualTo(DEFAULT_SAFE_MIN_VALUE);
    }

    @Test
    @Transactional
    void createEquipmentItemWithExistingId() throws Exception {
        // Create the EquipmentItem with an existing ID
        equipmentItem.setId(1L);
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        int databaseSizeBeforeCreate = equipmentItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEquipmentItems() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        // Get all the equipmentItemList
        restEquipmentItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstUseDate").value(hasItem(sameInstant(DEFAULT_FIRST_USE_DATE))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].amortization").value(hasItem(DEFAULT_AMORTIZATION.doubleValue())))
            .andExpect(jsonPath("$.[*].bareCode").value(hasItem(DEFAULT_BARE_CODE)))
            .andExpect(jsonPath("$.[*].safeMinValue").value(hasItem(DEFAULT_SAFE_MIN_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getEquipmentItem() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        // Get the equipmentItem
        restEquipmentItemMockMvc
            .perform(get(ENTITY_API_URL_ID, equipmentItem.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentItem.getId().intValue()))
            .andExpect(jsonPath("$.firstUseDate").value(sameInstant(DEFAULT_FIRST_USE_DATE)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.amortization").value(DEFAULT_AMORTIZATION.doubleValue()))
            .andExpect(jsonPath("$.bareCode").value(DEFAULT_BARE_CODE))
            .andExpect(jsonPath("$.safeMinValue").value(DEFAULT_SAFE_MIN_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEquipmentItem() throws Exception {
        // Get the equipmentItem
        restEquipmentItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquipmentItem() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();

        // Update the equipmentItem
        EquipmentItem updatedEquipmentItem = equipmentItemRepository.findById(equipmentItem.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentItem are not directly saved in db
        em.detach(updatedEquipmentItem);
        updatedEquipmentItem
            .firstUseDate(UPDATED_FIRST_USE_DATE)
            .price(UPDATED_PRICE)
            .amortization(UPDATED_AMORTIZATION)
            .bareCode(UPDATED_BARE_CODE)
            .safeMinValue(UPDATED_SAFE_MIN_VALUE);
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(updatedEquipmentItem);

        restEquipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
        EquipmentItem testEquipmentItem = equipmentItemList.get(equipmentItemList.size() - 1);
        assertThat(testEquipmentItem.getFirstUseDate()).isEqualTo(UPDATED_FIRST_USE_DATE);
        assertThat(testEquipmentItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEquipmentItem.getAmortization()).isEqualTo(UPDATED_AMORTIZATION);
        assertThat(testEquipmentItem.getBareCode()).isEqualTo(UPDATED_BARE_CODE);
        assertThat(testEquipmentItem.getSafeMinValue()).isEqualTo(UPDATED_SAFE_MIN_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipmentItemWithPatch() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();

        // Update the equipmentItem using partial update
        EquipmentItem partialUpdatedEquipmentItem = new EquipmentItem();
        partialUpdatedEquipmentItem.setId(equipmentItem.getId());

        partialUpdatedEquipmentItem.price(UPDATED_PRICE);

        restEquipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipmentItem.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipmentItem))
            )
            .andExpect(status().isOk());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
        EquipmentItem testEquipmentItem = equipmentItemList.get(equipmentItemList.size() - 1);
        assertThat(testEquipmentItem.getFirstUseDate()).isEqualTo(DEFAULT_FIRST_USE_DATE);
        assertThat(testEquipmentItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEquipmentItem.getAmortization()).isEqualTo(DEFAULT_AMORTIZATION);
        assertThat(testEquipmentItem.getBareCode()).isEqualTo(DEFAULT_BARE_CODE);
        assertThat(testEquipmentItem.getSafeMinValue()).isEqualTo(DEFAULT_SAFE_MIN_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateEquipmentItemWithPatch() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();

        // Update the equipmentItem using partial update
        EquipmentItem partialUpdatedEquipmentItem = new EquipmentItem();
        partialUpdatedEquipmentItem.setId(equipmentItem.getId());

        partialUpdatedEquipmentItem
            .firstUseDate(UPDATED_FIRST_USE_DATE)
            .price(UPDATED_PRICE)
            .amortization(UPDATED_AMORTIZATION)
            .bareCode(UPDATED_BARE_CODE)
            .safeMinValue(UPDATED_SAFE_MIN_VALUE);

        restEquipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipmentItem.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipmentItem))
            )
            .andExpect(status().isOk());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
        EquipmentItem testEquipmentItem = equipmentItemList.get(equipmentItemList.size() - 1);
        assertThat(testEquipmentItem.getFirstUseDate()).isEqualTo(UPDATED_FIRST_USE_DATE);
        assertThat(testEquipmentItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEquipmentItem.getAmortization()).isEqualTo(UPDATED_AMORTIZATION);
        assertThat(testEquipmentItem.getBareCode()).isEqualTo(UPDATED_BARE_CODE);
        assertThat(testEquipmentItem.getSafeMinValue()).isEqualTo(UPDATED_SAFE_MIN_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipmentItemDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipmentItem() throws Exception {
        int databaseSizeBeforeUpdate = equipmentItemRepository.findAll().size();
        equipmentItem.setId(count.incrementAndGet());

        // Create the EquipmentItem
        EquipmentItemDTO equipmentItemDTO = equipmentItemMapper.toDto(equipmentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipmentItem in the database
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipmentItem() throws Exception {
        // Initialize the database
        equipmentItemRepository.saveAndFlush(equipmentItem);

        int databaseSizeBeforeDelete = equipmentItemRepository.findAll().size();

        // Delete the equipmentItem
        restEquipmentItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipmentItem.getId()).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentItem> equipmentItemList = equipmentItemRepository.findAll();
        assertThat(equipmentItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

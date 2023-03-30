package com.example.GymInTheBack.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.SubCategory;
import com.example.GymInTheBack.repositories.EquipmentRepository;
import com.example.GymInTheBack.services.mappers.EquipmentMapper;
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
class EquipmentResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/equipment";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).imageUrl(DEFAULT_IMAGE_URL);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceTest.createEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        equipment.setSubCategory(subCategory);
        return equipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createUpdatedEntity(EntityManager em) {
        Equipment equipment = new Equipment().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).imageUrl(UPDATED_IMAGE_URL);
        // Add required entity
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            subCategory = SubCategoryResourceTest.createUpdatedEntity(em);
            em.persist(subCategory);
            em.flush();
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        equipment.setSubCategory(subCategory);
        return equipment;
    }

    @BeforeEach
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();
        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipment.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createEquipmentWithExistingId() throws Exception {
        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).get();
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).imageUrl(UPDATED_IMAGE_URL);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipment.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment.description(UPDATED_DESCRIPTION).imageUrl(UPDATED_IMAGE_URL);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipment.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateEquipmentWithPatch() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment using partial update
        Equipment partialUpdatedEquipment = new Equipment();
        partialUpdatedEquipment.setId(equipment.getId());

        partialUpdatedEquipment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).imageUrl(UPDATED_IMAGE_URL);

        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipment))
            )
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipment.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();
        equipment.setId(count.incrementAndGet());

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(equipmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Delete the equipment
        restEquipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

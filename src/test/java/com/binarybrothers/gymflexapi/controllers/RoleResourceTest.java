package com.binarybrothers.gymflexapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.binarybrothers.gymflexapi.dtos.role.RoleDTO;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.repositories.RoleRepository;
import com.binarybrothers.gymflexapi.services.auth.AuthenticationService;
import com.binarybrothers.gymflexapi.services.mappers.RoleMapper;
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
public class RoleResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION  = "BBBBBBBBBB";
    private static final Boolean DEFAULT_ANALYTICS = false;
    private static final Boolean UPDATED_ANALYTICS = true;

    private static final Boolean DEFAULT_MEMBERSHIP = false;
    private static final Boolean UPDATED_MEMBERSHIP = true;

    private static final Boolean DEFAULT_PAYMENT = false;
    private static final Boolean UPDATED_PAYMENT = true;

    private static final Boolean DEFAULT_INVENTORY = false;
    private static final Boolean UPDATED_INVENTORY = true;

    private static final Boolean DEFAULT_TRAINING = false;
    private static final Boolean UPDATED_TRAINING = true;

    private static final Boolean DEFAULT_SETTINGS = false;
    private static final Boolean UPDATED_SETTINGS = true;

    private static final Boolean DEFAULT_PREFERENCES = false;
    private static final Boolean UPDATED_PREFERENCES = true;

    private static final Boolean DEFAULT_MANAGE_WEB = false;
    private static final Boolean UPDATED_MANAGE_WEB = true;

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = Role.builder()

                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .analytics(DEFAULT_ANALYTICS)
                .membership(DEFAULT_INVENTORY)
                .payments(DEFAULT_PAYMENT)
                .inventory(DEFAULT_INVENTORY)
                .training(DEFAULT_TRAINING)
                .settings(DEFAULT_SETTINGS)
                .preferences(DEFAULT_PREFERENCES)
                .manageWebSite(DEFAULT_MANAGE_WEB)
                .build();
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = Role.builder()
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .analytics(UPDATED_ANALYTICS)
                .membership(UPDATED_INVENTORY)
                .payments(UPDATED_PAYMENT)
                .inventory(UPDATED_INVENTORY)
                .training(UPDATED_TRAINING)
                .settings(UPDATED_SETTINGS)
                .preferences(UPDATED_PREFERENCES)
                .manageWebSite(UPDATED_MANAGE_WEB)
                .build();
        return role;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        role = createEntity(em);
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
    void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        restRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
                .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRole.getAnalytics()).isEqualTo(DEFAULT_ANALYTICS);
        assertThat(testRole.getMembership()).isEqualTo(DEFAULT_MEMBERSHIP);
        assertThat(testRole.getPayments()).isEqualTo(DEFAULT_PAYMENT);
        assertThat(testRole.getInventory()).isEqualTo(DEFAULT_INVENTORY);
        assertThat(testRole.getSettings()).isEqualTo(DEFAULT_SETTINGS);
        assertThat(testRole.getPreferences()).isEqualTo(DEFAULT_PREFERENCES);
        assertThat(testRole.getManageWebSite()).isEqualTo(DEFAULT_MANAGE_WEB);
    }

    @Test
    @Transactional
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);
    }






    @Test
    @Transactional
    void getAllRoles() throws Exception {
        // Initialize the database
        Role roleUser = roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc
                .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(roleUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].analytics").value(hasItem(DEFAULT_ANALYTICS.booleanValue())))
                .andExpect(jsonPath("$.[*].membership").value(hasItem(DEFAULT_MEMBERSHIP.booleanValue())))
                .andExpect(jsonPath("$.[*].payments").value(hasItem(DEFAULT_MEMBERSHIP.booleanValue())))
                .andExpect(jsonPath("$.[*].inventory").value(hasItem(DEFAULT_INVENTORY.booleanValue())))
                .andExpect(jsonPath("$.[*].training").value(hasItem(DEFAULT_TRAINING.booleanValue())))
                .andExpect(jsonPath("$.[*].settings").value(hasItem(DEFAULT_SETTINGS.booleanValue())))
                .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES.booleanValue())))
                .andExpect(jsonPath("$.[*].manageWebSite").value(hasItem(DEFAULT_MANAGE_WEB.booleanValue()))
                );
    }

    @Test
    @Transactional
    void getRole() throws Exception {
        // Initialize the database
        Role newRole = roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc
                .perform(get(ENTITY_API_URL_ID, newRole.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(newRole.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.analytics").value(DEFAULT_ANALYTICS.booleanValue()))
                .andExpect(jsonPath("$.membership").value(DEFAULT_MEMBERSHIP.booleanValue()))
                .andExpect(jsonPath("$.payments").value(DEFAULT_MEMBERSHIP.booleanValue()))
                .andExpect(jsonPath("$.inventory").value(DEFAULT_INVENTORY.booleanValue()))
                .andExpect(jsonPath("$.training").value(DEFAULT_TRAINING.booleanValue()))
                .andExpect(jsonPath("$.settings").value(DEFAULT_SETTINGS.booleanValue()))
                .andExpect(jsonPath("$.preferences").value(DEFAULT_PREFERENCES.booleanValue()))
                .andExpect(jsonPath("$.manageWebSite").value(DEFAULT_MANAGE_WEB.booleanValue())

                ).andDo(print());
    }


    @Test
    @Transactional
    void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategory() throws Exception {
        // Initialize the database
        Role userRole = roleRepository.saveAndFlush(role);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(userRole.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole=Role.builder()
                .id(updatedRole.getId())
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .analytics(UPDATED_ANALYTICS)
                .membership(UPDATED_INVENTORY)
                .payments(UPDATED_PAYMENT)
                .inventory(UPDATED_INVENTORY)
                .training(UPDATED_TRAINING)
                .settings(UPDATED_SETTINGS)
                .preferences(UPDATED_PREFERENCES)
                .manageWebSite(UPDATED_MANAGE_WEB)
                .build();
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        restRoleMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, roleDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                                .content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRole.getAnalytics()).isEqualTo(UPDATED_ANALYTICS);
        assertThat(testRole.getMembership()).isEqualTo(UPDATED_MEMBERSHIP);
        assertThat(testRole.getPayments()).isEqualTo(UPDATED_PAYMENT);
        assertThat(testRole.getInventory()).isEqualTo(UPDATED_INVENTORY);
        assertThat(testRole.getSettings()).isEqualTo(UPDATED_SETTINGS);
        assertThat(testRole.getPreferences()).isEqualTo(UPDATED_PREFERENCES);
        assertThat(testRole.getManageWebSite()).isEqualTo(UPDATED_MANAGE_WEB);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, roleDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                                .content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                                .content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(roleDTO)))
                .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }





    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, roleDTO.getId())
                                .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                                .content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                                .content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        role.setId(count.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleMockMvc
                .perform(
                        patch(ENTITY_API_URL).contentType("application/merge-patch+json").header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(roleDTO))
                )
                .andExpect(status().isMethodNotAllowed());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRole() throws Exception {
        // Initialize the database
        Role userRole = roleRepository.saveAndFlush(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc
                .perform(delete(ENTITY_API_URL_ID, userRole.getId()).accept(MediaType.TEXT_PLAIN_VALUE).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
    }

}

package com.example.GymInTheBack.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.services.mappers.OnlineUserMapper;
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
 * Integration tests for the {@link OnlineUserResource} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OnlineUserResourceTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "GRS.+F@NRIFHS\\\\nLYFO";
    private static final String UPDATED_EMAIL = "V@.PZ\\\\yVOYP";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/online-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OnlineUserRepository onlineUserRepository;

    @Autowired
    private OnlineUserMapper onlineUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOnlineUserMockMvc;

    private OnlineUser onlineUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OnlineUser createEntity(EntityManager em) {
        OnlineUser onlineUser = new OnlineUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .login(DEFAULT_LOGIN)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .profilePicture(DEFAULT_PROFILE_PICTURE);
        return onlineUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OnlineUser createUpdatedEntity(EntityManager em) {
        OnlineUser onlineUser = new OnlineUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .profilePicture(UPDATED_PROFILE_PICTURE);
        return onlineUser;
    }

    @BeforeEach
    public void initTest() {
        onlineUser = createEntity(em);
    }

    @Test
    @Transactional
    void createOnlineUser() throws Exception {
        int databaseSizeBeforeCreate = onlineUserRepository.findAll().size();
        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);
        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isCreated());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeCreate + 1);
        OnlineUser testOnlineUser = onlineUserList.get(onlineUserList.size() - 1);
        assertThat(testOnlineUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOnlineUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOnlineUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testOnlineUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOnlineUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testOnlineUser.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
    }

    @Test
    @Transactional
    void createOnlineUserWithExistingId() throws Exception {
        // Create the OnlineUser with an existing ID
        onlineUser.setId(1L);
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        int databaseSizeBeforeCreate = onlineUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = onlineUserRepository.findAll().size();
        // set the field null
        onlineUser.setFirstName(null);

        // Create the OnlineUser, which fails.
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = onlineUserRepository.findAll().size();
        // set the field null
        onlineUser.setLastName(null);

        // Create the OnlineUser, which fails.
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = onlineUserRepository.findAll().size();
        // set the field null
        onlineUser.setLogin(null);

        // Create the OnlineUser, which fails.
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = onlineUserRepository.findAll().size();
        // set the field null
        onlineUser.setEmail(null);

        // Create the OnlineUser, which fails.
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = onlineUserRepository.findAll().size();
        // set the field null
        onlineUser.setPassword(null);

        // Create the OnlineUser, which fails.
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        restOnlineUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isBadRequest());

        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOnlineUsers() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        // Get all the onlineUserList
        restOnlineUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(onlineUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(DEFAULT_PROFILE_PICTURE)));
    }

    @Test
    @Transactional
    void getOnlineUser() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        // Get the onlineUser
        restOnlineUserMockMvc
            .perform(get(ENTITY_API_URL_ID, onlineUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(onlineUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.profilePicture").value(DEFAULT_PROFILE_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingOnlineUser() throws Exception {
        // Get the onlineUser
        restOnlineUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOnlineUser() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();

        // Update the onlineUser
        OnlineUser updatedOnlineUser = onlineUserRepository.findById(onlineUser.getId()).get();
        // Disconnect from session so that the updates on updatedOnlineUser are not directly saved in db
        em.detach(updatedOnlineUser);
        updatedOnlineUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .profilePicture(UPDATED_PROFILE_PICTURE);
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(updatedOnlineUser);

        restOnlineUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, onlineUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
        OnlineUser testOnlineUser = onlineUserList.get(onlineUserList.size() - 1);
        assertThat(testOnlineUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOnlineUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOnlineUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testOnlineUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOnlineUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOnlineUser.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
    }

    @Test
    @Transactional
    void putNonExistingOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, onlineUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(onlineUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOnlineUserWithPatch() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();

        // Update the onlineUser using partial update
        OnlineUser partialUpdatedOnlineUser = new OnlineUser();
        partialUpdatedOnlineUser.setId(onlineUser.getId());

        partialUpdatedOnlineUser.lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL).profilePicture(UPDATED_PROFILE_PICTURE);

        restOnlineUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOnlineUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOnlineUser))
            )
            .andExpect(status().isOk());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
        OnlineUser testOnlineUser = onlineUserList.get(onlineUserList.size() - 1);
        assertThat(testOnlineUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOnlineUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOnlineUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testOnlineUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOnlineUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testOnlineUser.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
    }

    @Test
    @Transactional
    void fullUpdateOnlineUserWithPatch() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();

        // Update the onlineUser using partial update
        OnlineUser partialUpdatedOnlineUser = new OnlineUser();
        partialUpdatedOnlineUser.setId(onlineUser.getId());

        partialUpdatedOnlineUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .profilePicture(UPDATED_PROFILE_PICTURE);

        restOnlineUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOnlineUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOnlineUser))
            )
            .andExpect(status().isOk());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
        OnlineUser testOnlineUser = onlineUserList.get(onlineUserList.size() - 1);
        assertThat(testOnlineUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOnlineUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOnlineUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testOnlineUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOnlineUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testOnlineUser.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
    }

    @Test
    @Transactional
    void patchNonExistingOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, onlineUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOnlineUser() throws Exception {
        int databaseSizeBeforeUpdate = onlineUserRepository.findAll().size();
        onlineUser.setId(count.incrementAndGet());

        // Create the OnlineUser
        OnlineUserDTO onlineUserDTO = onlineUserMapper.toDto(onlineUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOnlineUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(onlineUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OnlineUser in the database
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOnlineUser() throws Exception {
        // Initialize the database
        onlineUserRepository.saveAndFlush(onlineUser);

        int databaseSizeBeforeDelete = onlineUserRepository.findAll().size();

        // Delete the onlineUser
        restOnlineUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, onlineUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OnlineUser> onlineUserList = onlineUserRepository.findAll();
        assertThat(onlineUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

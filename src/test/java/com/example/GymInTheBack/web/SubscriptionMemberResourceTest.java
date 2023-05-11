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

import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SubscriptionMemberRepository;
import com.example.GymInTheBack.services.auth.AuthenticationService;
import com.example.GymInTheBack.services.mappers.SubscriptionMemberMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubscriptionMemberResource} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionMemberResourceTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CODE_SUBSCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_SUBSCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_DISCOUNT_PERCENTAGE = 1F;
    private static final Float UPDATED_DISCOUNT_PERCENTAGE = 2F;

    private static final String ENTITY_API_URL = "/api/subscription-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionMemberRepository subscriptionMemberRepository;

    @Autowired
    private SubscriptionMemberMapper subscriptionMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionMemberMockMvc;

    private SubscriptionMember subscriptionMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionMember createEntity(EntityManager em) {
        SubscriptionMember subscriptionMember = new SubscriptionMember()
            .startDate(DEFAULT_START_DATE)
            .codeSubscription(DEFAULT_CODE_SUBSCRIPTION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        subscriptionMember.setMember(member);
        // Add required entity
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            plan = PlanResourceTest.createEntity(em);
            em.persist(plan);
            em.flush();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        subscriptionMember.setPlan(plan);
        return subscriptionMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionMember createUpdatedEntity(EntityManager em) {
        SubscriptionMember subscriptionMember = new SubscriptionMember()
            .startDate(UPDATED_START_DATE)
            .codeSubscription(UPDATED_CODE_SUBSCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        subscriptionMember.setMember(member);
        // Add required entity
        Plan plan;
        if (TestUtil.findAll(em, Plan.class).isEmpty()) {
            plan = PlanResourceTest.createUpdatedEntity(em);
            em.persist(plan);
            em.flush();
        } else {
            plan = TestUtil.findAll(em, Plan.class).get(0);
        }
        subscriptionMember.setPlan(plan);
        return subscriptionMember;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        subscriptionMember = createEntity(em);
        RegisterRequest request = new RegisterRequest("testFirstName","testLastName","testLogin","test@gmail.com","testPassword");

        Role roleUser = Role.builder()
                .name("ClientVisiter")
                .description("For client that visit our site and sign up")
                .membership(true)
                .build();
        AuthenticationResponse authenticationResponse = authenticationService.register(request,roleUser);
        token=authenticationResponse.getAccessToken();
    }

    @Test
    @Transactional
    void createSubscriptionMember() throws Exception {
        int databaseSizeBeforeCreate = subscriptionMemberRepository.findAll().size();
        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);
        restSubscriptionMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionMember testSubscriptionMember = subscriptionMemberList.get(subscriptionMemberList.size() - 1);
        assertThat(testSubscriptionMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSubscriptionMember.getCodeSubscription()).isEqualTo(DEFAULT_CODE_SUBSCRIPTION);
        assertThat(testSubscriptionMember.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    void createSubscriptionMemberWithExistingId() throws Exception {
        // Create the SubscriptionMember with an existing ID
        subscriptionMember.setId(1L);
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        int databaseSizeBeforeCreate = subscriptionMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionMemberRepository.findAll().size();
        // set the field null
        subscriptionMember.setStartDate(null);

        // Create the SubscriptionMember, which fails.
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        restSubscriptionMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionMembers() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        // Get all the subscriptionMemberList
        restSubscriptionMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].codeSubscription").value(hasItem(DEFAULT_CODE_SUBSCRIPTION)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())));
    }

    @Test
    @Transactional
    void getSubscriptionMember() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        // Get the subscriptionMember
        restSubscriptionMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionMember.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionMember.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.codeSubscription").value(DEFAULT_CODE_SUBSCRIPTION))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionMember() throws Exception {
        // Get the subscriptionMember
        restSubscriptionMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE).header("Authorization", "Bearer " + token)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionMember() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();

        // Update the subscriptionMember
        SubscriptionMember updatedSubscriptionMember = subscriptionMemberRepository.findById(subscriptionMember.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriptionMember are not directly saved in db
        em.detach(updatedSubscriptionMember);
        updatedSubscriptionMember
            .startDate(UPDATED_START_DATE)
            .codeSubscription(UPDATED_CODE_SUBSCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(updatedSubscriptionMember);

        restSubscriptionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionMember testSubscriptionMember = subscriptionMemberList.get(subscriptionMemberList.size() - 1);
        assertThat(testSubscriptionMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscriptionMember.getCodeSubscription()).isEqualTo(UPDATED_CODE_SUBSCRIPTION);
        assertThat(testSubscriptionMember.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionMemberWithPatch() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();

        // Update the subscriptionMember using partial update
        SubscriptionMember partialUpdatedSubscriptionMember = new SubscriptionMember();
        partialUpdatedSubscriptionMember.setId(subscriptionMember.getId());

        partialUpdatedSubscriptionMember.discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);

        restSubscriptionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionMember.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionMember))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionMember testSubscriptionMember = subscriptionMemberList.get(subscriptionMemberList.size() - 1);
        assertThat(testSubscriptionMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSubscriptionMember.getCodeSubscription()).isEqualTo(DEFAULT_CODE_SUBSCRIPTION);
        assertThat(testSubscriptionMember.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionMemberWithPatch() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();

        // Update the subscriptionMember using partial update
        SubscriptionMember partialUpdatedSubscriptionMember = new SubscriptionMember();
        partialUpdatedSubscriptionMember.setId(subscriptionMember.getId());

        partialUpdatedSubscriptionMember
            .startDate(UPDATED_START_DATE)
            .codeSubscription(UPDATED_CODE_SUBSCRIPTION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE);

        restSubscriptionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionMember.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionMember))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionMember testSubscriptionMember = subscriptionMemberList.get(subscriptionMemberList.size() - 1);
        assertThat(testSubscriptionMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscriptionMember.getCodeSubscription()).isEqualTo(UPDATED_CODE_SUBSCRIPTION);
        assertThat(testSubscriptionMember.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionMemberDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionMember() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionMemberRepository.findAll().size();
        subscriptionMember.setId(count.incrementAndGet());

        // Create the SubscriptionMember
        SubscriptionMemberDTO subscriptionMemberDTO = subscriptionMemberMapper.toDto(subscriptionMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionMember in the database
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionMember() throws Exception {
        // Initialize the database
        subscriptionMemberRepository.saveAndFlush(subscriptionMember);

        int databaseSizeBeforeDelete = subscriptionMemberRepository.findAll().size();

        // Delete the subscriptionMember
        restSubscriptionMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionMember.getId()).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionMember> subscriptionMemberList = subscriptionMemberRepository.findAll();
        assertThat(subscriptionMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

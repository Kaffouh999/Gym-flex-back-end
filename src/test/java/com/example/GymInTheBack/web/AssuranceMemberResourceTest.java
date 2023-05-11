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
import java.util.concurrent.atomic.AtomicLong;;
import com.example.GymInTheBack.dtos.assuranceMember.AssuranceMemberDTO;
import com.example.GymInTheBack.entities.AssuranceMember;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.repositories.AssuranceMemberRepository;
import com.example.GymInTheBack.services.auth.AuthenticationService;
import com.example.GymInTheBack.services.mappers.AssuranceMemberMapper;
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
 * Integration tests for the {@link AssuranceMemberResource} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AssuranceMemberResourceTest {

    private static final Double DEFAULT_AMOUNT_PAYED = 1D;
    private static final Double UPDATED_AMOUNT_PAYED = 2D;

    private static final String DEFAULT_ASSURANC_AGENCY = "AAAAAAAAAA";
    private static final String UPDATED_ASSURANC_AGENCY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/assurance-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static String token="";

    @Autowired
    private AuthenticationService authenticationService;
    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssuranceMemberRepository assuranceMemberRepository;

    @Autowired
    private AssuranceMemberMapper assuranceMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssuranceMemberMockMvc;

    private AssuranceMember assuranceMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssuranceMember createEntity(EntityManager em) {
        AssuranceMember assuranceMember = new AssuranceMember()
            .amountPayed(DEFAULT_AMOUNT_PAYED)
            .assurancAgency(DEFAULT_ASSURANC_AGENCY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        assuranceMember.setMember(member);
        return assuranceMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssuranceMember createUpdatedEntity(EntityManager em) {
        AssuranceMember assuranceMember = new AssuranceMember()
            .amountPayed(UPDATED_AMOUNT_PAYED)
            .assurancAgency(UPDATED_ASSURANC_AGENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceTest.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        assuranceMember.setMember(member);
        return assuranceMember;
    }

    @BeforeEach
    public void initTest() throws MessagingException {
        assuranceMember = createEntity(em);
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
    void createAssuranceMember() throws Exception {
        int databaseSizeBeforeCreate = assuranceMemberRepository.findAll().size();
        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);
        restAssuranceMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeCreate + 1);
        AssuranceMember testAssuranceMember = assuranceMemberList.get(assuranceMemberList.size() - 1);
        assertThat(testAssuranceMember.getAmountPayed()).isEqualTo(DEFAULT_AMOUNT_PAYED);
        assertThat(testAssuranceMember.getAssurancAgency()).isEqualTo(DEFAULT_ASSURANC_AGENCY);
        assertThat(testAssuranceMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAssuranceMember.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createAssuranceMemberWithExistingId() throws Exception {
        // Create the AssuranceMember with an existing ID
        assuranceMember.setId(1L);
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        int databaseSizeBeforeCreate = assuranceMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuranceMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountPayedIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuranceMemberRepository.findAll().size();
        // set the field null
        assuranceMember.setAmountPayed(null);

        // Create the AssuranceMember, which fails.
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        restAssuranceMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuranceMemberRepository.findAll().size();
        // set the field null
        assuranceMember.setStartDate(null);

        // Create the AssuranceMember, which fails.
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        restAssuranceMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuranceMemberRepository.findAll().size();
        // set the field null
        assuranceMember.setEndDate(null);

        // Create the AssuranceMember, which fails.
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        restAssuranceMemberMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssuranceMembers() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        // Get all the assuranceMemberList
        restAssuranceMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assuranceMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountPayed").value(hasItem(DEFAULT_AMOUNT_PAYED.doubleValue())))
            .andExpect(jsonPath("$.[*].assurancAgency").value(hasItem(DEFAULT_ASSURANC_AGENCY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }

    @Test
    @Transactional
    void getAssuranceMember() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        // Get the assuranceMember
        restAssuranceMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, assuranceMember.getId()).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assuranceMember.getId().intValue()))
            .andExpect(jsonPath("$.amountPayed").value(DEFAULT_AMOUNT_PAYED.doubleValue()))
            .andExpect(jsonPath("$.assurancAgency").value(DEFAULT_ASSURANC_AGENCY))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingAssuranceMember() throws Exception {
        // Get the assuranceMember
        restAssuranceMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssuranceMember() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();

        // Update the assuranceMember
        AssuranceMember updatedAssuranceMember = assuranceMemberRepository.findById(assuranceMember.getId()).get();
        // Disconnect from session so that the updates on updatedAssuranceMember are not directly saved in db
        em.detach(updatedAssuranceMember);
        updatedAssuranceMember
            .amountPayed(UPDATED_AMOUNT_PAYED)
            .assurancAgency(UPDATED_ASSURANC_AGENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(updatedAssuranceMember);

        restAssuranceMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
        AssuranceMember testAssuranceMember = assuranceMemberList.get(assuranceMemberList.size() - 1);
        assertThat(testAssuranceMember.getAmountPayed()).isEqualTo(UPDATED_AMOUNT_PAYED);
        assertThat(testAssuranceMember.getAssurancAgency()).isEqualTo(UPDATED_ASSURANC_AGENCY);
        assertThat(testAssuranceMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAssuranceMember.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token).content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssuranceMemberWithPatch() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();

        // Update the assuranceMember using partial update
        AssuranceMember partialUpdatedAssuranceMember = new AssuranceMember();
        partialUpdatedAssuranceMember.setId(assuranceMember.getId());

        partialUpdatedAssuranceMember.assurancAgency(UPDATED_ASSURANC_AGENCY).endDate(UPDATED_END_DATE);

        restAssuranceMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssuranceMember.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssuranceMember))
            )
            .andExpect(status().isOk());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
        AssuranceMember testAssuranceMember = assuranceMemberList.get(assuranceMemberList.size() - 1);
        assertThat(testAssuranceMember.getAmountPayed()).isEqualTo(DEFAULT_AMOUNT_PAYED);
        assertThat(testAssuranceMember.getAssurancAgency()).isEqualTo(UPDATED_ASSURANC_AGENCY);
        assertThat(testAssuranceMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAssuranceMember.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAssuranceMemberWithPatch() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();

        // Update the assuranceMember using partial update
        AssuranceMember partialUpdatedAssuranceMember = new AssuranceMember();
        partialUpdatedAssuranceMember.setId(assuranceMember.getId());

        partialUpdatedAssuranceMember
            .amountPayed(UPDATED_AMOUNT_PAYED)
            .assurancAgency(UPDATED_ASSURANC_AGENCY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restAssuranceMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssuranceMember.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssuranceMember))
            )
            .andExpect(status().isOk());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
        AssuranceMember testAssuranceMember = assuranceMemberList.get(assuranceMemberList.size() - 1);
        assertThat(testAssuranceMember.getAmountPayed()).isEqualTo(UPDATED_AMOUNT_PAYED);
        assertThat(testAssuranceMember.getAssurancAgency()).isEqualTo(UPDATED_ASSURANC_AGENCY);
        assertThat(testAssuranceMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAssuranceMember.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assuranceMemberDTO.getId())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssuranceMember() throws Exception {
        int databaseSizeBeforeUpdate = assuranceMemberRepository.findAll().size();
        assuranceMember.setId(count.incrementAndGet());

        // Create the AssuranceMember
        AssuranceMemberDTO assuranceMemberDTO = assuranceMemberMapper.toDto(assuranceMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json").header("Authorization", "Bearer " + token)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssuranceMember in the database
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssuranceMember() throws Exception {
        // Initialize the database
        assuranceMemberRepository.saveAndFlush(assuranceMember);

        int databaseSizeBeforeDelete = assuranceMemberRepository.findAll().size();

        // Delete the assuranceMember
        restAssuranceMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, assuranceMember.getId()).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssuranceMember> assuranceMemberList = assuranceMemberRepository.findAll();
        assertThat(assuranceMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

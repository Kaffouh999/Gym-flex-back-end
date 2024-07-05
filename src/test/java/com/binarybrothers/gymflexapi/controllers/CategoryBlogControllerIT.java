package com.binarybrothers.gymflexapi.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.binarybrothers.gymflexapi.dtos.blogs.CategoryBlogDTO;
import com.binarybrothers.gymflexapi.entities.CategoryBlog;
import com.binarybrothers.gymflexapi.repositories.CategoryBlogRepository;
import com.binarybrothers.gymflexapi.services.mappers.CategoryBlogMapper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;


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
 * Integration tests for the {@link CategoryBlogController} REST controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryBlogControllerIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-blogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryBlogRepository categoryBlogRepository;

    @Autowired
    private CategoryBlogMapper categoryBlogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryBlogMockMvc;

    private CategoryBlog categoryBlog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryBlog createEntity(EntityManager em) {
        CategoryBlog categoryBlog = new CategoryBlog().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return categoryBlog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryBlog createUpdatedEntity(EntityManager em) {
        CategoryBlog categoryBlog = new CategoryBlog().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return categoryBlog;
    }

    @BeforeEach
    public void initTest() {
        categoryBlog = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoryBlog() throws Exception {
        int databaseSizeBeforeCreate = categoryBlogRepository.findAll().size();
        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);
        restCategoryBlogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryBlog testCategoryBlog = categoryBlogList.get(categoryBlogList.size() - 1);
        assertThat(testCategoryBlog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategoryBlog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCategoryBlogWithExistingId() throws Exception {
        // Create the CategoryBlog with an existing ID
        categoryBlog.setId(1L);
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        int databaseSizeBeforeCreate = categoryBlogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryBlogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryBlogRepository.findAll().size();
        // set the field null
        categoryBlog.setName(null);

        // Create the CategoryBlog, which fails.
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        restCategoryBlogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryBlogs() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        // Get all the categoryBlogList
        restCategoryBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryBlog.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryBlog() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        // Get the categoryBlog
        restCategoryBlogMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryBlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryBlog.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCategoryBlog() throws Exception {
        // Get the categoryBlog
        restCategoryBlogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryBlog() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();

        // Update the categoryBlog
        CategoryBlog updatedCategoryBlog = categoryBlogRepository.findById(categoryBlog.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryBlog are not directly saved in db
        em.detach(updatedCategoryBlog);
        updatedCategoryBlog.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(updatedCategoryBlog);

        restCategoryBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryBlogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
        CategoryBlog testCategoryBlog = categoryBlogList.get(categoryBlogList.size() - 1);
        assertThat(testCategoryBlog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoryBlog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryBlogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryBlogWithPatch() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();

        // Update the categoryBlog using partial update
        CategoryBlog partialUpdatedCategoryBlog = new CategoryBlog();
        partialUpdatedCategoryBlog.setId(categoryBlog.getId());

        partialUpdatedCategoryBlog.description(UPDATED_DESCRIPTION);

        restCategoryBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryBlog))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
        CategoryBlog testCategoryBlog = categoryBlogList.get(categoryBlogList.size() - 1);
        assertThat(testCategoryBlog.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategoryBlog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCategoryBlogWithPatch() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();

        // Update the categoryBlog using partial update
        CategoryBlog partialUpdatedCategoryBlog = new CategoryBlog();
        partialUpdatedCategoryBlog.setId(categoryBlog.getId());

        partialUpdatedCategoryBlog.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCategoryBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryBlog))
            )
            .andExpect(status().isOk());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
        CategoryBlog testCategoryBlog = categoryBlogList.get(categoryBlogList.size() - 1);
        assertThat(testCategoryBlog.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoryBlog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryBlogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryBlog() throws Exception {
        int databaseSizeBeforeUpdate = categoryBlogRepository.findAll().size();
        categoryBlog.setId(count.incrementAndGet());

        // Create the CategoryBlog
        CategoryBlogDTO categoryBlogDTO = categoryBlogMapper.toDto(categoryBlog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryBlogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryBlogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryBlog in the database
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryBlog() throws Exception {
        // Initialize the database
        categoryBlogRepository.saveAndFlush(categoryBlog);

        int databaseSizeBeforeDelete = categoryBlogRepository.findAll().size();

        // Delete the categoryBlog
        restCategoryBlogMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryBlog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoryBlog> categoryBlogList = categoryBlogRepository.findAll();
        assertThat(categoryBlogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

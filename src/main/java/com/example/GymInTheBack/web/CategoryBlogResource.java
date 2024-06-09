package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.blogs.CategoryBlogDTO;
import com.example.GymInTheBack.repositories.CategoryBlogRepository;
import com.example.GymInTheBack.services.blogs.CategoryBlogService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryBlogResource {

    private final Logger log = LoggerFactory.getLogger(CategoryBlogResource.class);

    private static final String ENTITY_NAME = "categoryBlog";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final CategoryBlogService categoryBlogService;

    private final CategoryBlogRepository categoryBlogRepository;

    public CategoryBlogResource(CategoryBlogService categoryBlogService, CategoryBlogRepository categoryBlogRepository) {
        this.categoryBlogService = categoryBlogService;
        this.categoryBlogRepository = categoryBlogRepository;
    }

    /**
     * {@code POST  /category-blogs} : Create a new categoryBlog.
     *
     * @param categoryBlogDTO the categoryBlogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryBlogDTO, or with status {@code 400 (Bad Request)} if the categoryBlog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/category-blogs")
    public ResponseEntity<CategoryBlogDTO> createCategoryBlog(@Valid @RequestBody CategoryBlogDTO categoryBlogDTO) throws URISyntaxException {
        log.debug("REST request to save CategoryBlog : {}", categoryBlogDTO);
        if (categoryBlogDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryBlog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (categoryBlogDTO.getName() == null || categoryBlogDTO.getName().isEmpty()) {
            throw new BadRequestAlertException("A new categoryBlog must have name required", ENTITY_NAME, "namerequired");
        }
        CategoryBlogDTO result = categoryBlogService.save(categoryBlogDTO);
        return ResponseEntity.created(new URI("/api/category-blogs/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /category-blogs/:id} : Updates an existing categoryBlog.
     *
     * @param id              the id of the categoryBlogDTO to save.
     * @param categoryBlogDTO the categoryBlogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryBlogDTO,
     * or with status {@code 400 (Bad Request)} if the categoryBlogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryBlogDTO couldn't be updated.
     */
    @PutMapping("/category-blogs/{id}")
    public ResponseEntity<CategoryBlogDTO> updateCategoryBlog(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody CategoryBlogDTO categoryBlogDTO) {
        log.debug("REST request to update CategoryBlog : {}, {}", id, categoryBlogDTO);
        if (categoryBlogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryBlogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryBlogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoryBlogDTO result = categoryBlogService.update(categoryBlogDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, categoryBlogDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /category-blogs/:id} : Partial updates given fields of an existing categoryBlog, field will ignore if it is null
     *
     * @param id              the id of the categoryBlogDTO to save.
     * @param categoryBlogDTO the categoryBlogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryBlogDTO,
     * or with status {@code 400 (Bad Request)} if the categoryBlogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryBlogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryBlogDTO couldn't be updated.
     */
    @PatchMapping(value = "/category-blogs/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<CategoryBlogDTO> partialUpdateCategoryBlog(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody CategoryBlogDTO categoryBlogDTO) {
        log.debug("REST request to partial update CategoryBlog partially : {}, {}", id, categoryBlogDTO);
        if (categoryBlogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryBlogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryBlogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryBlogDTO> result = categoryBlogService.partialUpdate(categoryBlogDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, categoryBlogDTO.getId().toString()));
    }

    /**
     * {@code GET  /category-blogs} : get all the categoryBlogs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryBlogs in body.
     */
    @GetMapping("/category-blogs")
    public List<CategoryBlogDTO> getAllCategoryBlogs() {
        log.debug("REST request to get all CategoryBlogs");
        return categoryBlogService.findAll();
    }

    /**
     * {@code GET  /category-blogs/:id} : get the "id" categoryBlog.
     *
     * @param id the id of the categoryBlogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryBlogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/category-blogs/{id}")
    public ResponseEntity<CategoryBlogDTO> getCategoryBlog(@PathVariable Long id) {
        log.debug("REST request to get CategoryBlog : {}", id);
        Optional<CategoryBlogDTO> categoryBlogDTO = categoryBlogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryBlogDTO);
    }

    /**
     * {@code DELETE  /category-blogs/:id} : delete the "id" categoryBlog.
     *
     * @param id the id of the categoryBlogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/category-blogs/{id}")
    public ResponseEntity<Void> deleteCategoryBlog(@PathVariable Long id) {
        log.debug("REST request to delete CategoryBlog : {}", id);
        categoryBlogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}

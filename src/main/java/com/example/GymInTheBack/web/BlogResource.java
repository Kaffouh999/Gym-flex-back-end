package com.example.GymInTheBack.web;

import com.example.GymInTheBack.dtos.blogs.BlogDTO;
import com.example.GymInTheBack.repositories.BlogRepository;
import com.example.GymInTheBack.services.blogs.BlogService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing blogs.
 */
@RestController
@RequestMapping("/api")
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    private static final String ENTITY_NAME = "blog";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final BlogService blogService;
    private final BlogRepository blogRepository;

    public BlogResource(BlogService blogService, BlogRepository blogRepository) {
        this.blogService = blogService;
        this.blogRepository = blogRepository;
    }

    /**
     * POST  /blogs : Create a new blog.
     *
     * @param blogDTO the blogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blogDTO, or with status 400 (Bad Request) if the blog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blogs")
    public ResponseEntity<BlogDTO> createBlog(@Valid @RequestBody BlogDTO blogDTO) throws URISyntaxException {
        log.debug("REST request to save Blog : {}", blogDTO);
        if (blogDTO.getId() != null) {
            throw new BadRequestAlertException("A new blog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogDTO result = blogService.save(blogDTO);
        return ResponseEntity.created(new URI("/api/blogs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /blogs/:id : Updates an existing blog.
     *
     * @param id      the id of the blogDTO to save
     * @param blogDTO the blogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blogDTO,
     * or with status 400 (Bad Request) if the blogDTO is not valid,
     * or with status 500 (Internal Server Error) if the blogDTO couldn't be updated
     */
    @PutMapping("/blogs/{id}")
    public ResponseEntity<BlogDTO> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogDTO blogDTO) {
        log.debug("REST request to update Blog : {}, {}", id, blogDTO);
        validateBlogId(blogDTO.getId(), id);

        BlogDTO result = blogService.update(blogDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogDTO.getId().toString()))
                .body(result);
    }

    /**
     * PATCH  /blogs/:id : Partial updates given fields of an existing blog, field will ignore if it is null
     *
     * @param id      the id of the blogDTO to save
     * @param blogDTO the blogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blogDTO,
     * or with status 400 (Bad Request) if the blogDTO is not valid,
     * or with status 404 (Not Found) if the blogDTO is not found,
     * or with status 500 (Internal Server Error) if the blogDTO couldn't be updated
     */
    @PatchMapping(value = "/blogs/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<BlogDTO> partialUpdateBlog(@PathVariable Long id, @NotNull @RequestBody BlogDTO blogDTO) {
        log.debug("REST request to partial update Blog partially : {}, {}", id, blogDTO);
        validateBlogId(blogDTO.getId(), id);

        Optional<BlogDTO> result = blogService.partialUpdate(blogDTO);
        return ResponseUtil.wrapOrNotFound(result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blogDTO.getId().toString()));
    }

    /**
     * GET  /blogs : get all the blogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blogs in body
     */
    @GetMapping("/blogs")
    public List<BlogDTO> getAllBlogs() {
        log.debug("REST request to get all Blogs");
        return blogService.findAll();
    }

    /**
     * GET  /blogs/:id : get the "id" blog.
     *
     * @param id the id of the blogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/blogs/{id}")
    public ResponseEntity<BlogDTO> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Blog : {}", id);
        Optional<BlogDTO> blogDTO = blogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blogDTO);
    }

    /**
     * DELETE  /blogs/:id : delete the "id" blog.
     *
     * @param id the id of the blogDTO to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Blog : {}", id);
        blogService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    private void validateBlogId(Long blogDTOId, Long id) {
        if (blogDTOId == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blogDTOId)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!blogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }
}

package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.subCategory.SubCategoryDTO;
import com.example.GymInTheBack.repositories.SubCategoryRepository;
import com.example.GymInTheBack.services.subCategory.SubCategoryService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class SubCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";


    private String applicationName="GymFlex";

    private final SubCategoryService subCategoryService;

    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryResource(SubCategoryService subCategoryService, SubCategoryRepository subCategoryRepository) {
        this.subCategoryService = subCategoryService;
        this.subCategoryRepository = subCategoryRepository;
    }

    /**
     * {@code POST  /sub-categories} : Create a new subCategory.
     *
     * @param subCategoryDTO the subCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategoryDTO, or with status {@code 400 (Bad Request)} if the subCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-categories")
    public ResponseEntity<SubCategoryDTO> createSubCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save SubCategory : {}", subCategoryDTO);
        /*if (subCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (subCategoryDTO.getName() == null || subCategoryDTO.getName().trim().equals("") ) {
            throw new BadRequestAlertException("A new subCategory should have a name", ENTITY_NAME, "namerequiered");
        }*/
        SubCategoryDTO result = subCategoryService.save(subCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/sub-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-categories/:id} : Updates an existing subCategory.
     *
     * @param id the id of the subCategoryDTO to save.
     * @param subCategoryDTO the subCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the subCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-categories/{id}")
    public ResponseEntity<SubCategoryDTO> updateSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubCategoryDTO subCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubCategory : {}, {}", id, subCategoryDTO);
        if (subCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubCategoryDTO result = subCategoryService.update(subCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-categories/:id} : Partial updates given fields of an existing subCategory, field will ignore if it is null
     *
     * @param id the id of the subCategoryDTO to save.
     * @param subCategoryDTO the subCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the subCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubCategoryDTO> partialUpdateSubCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubCategoryDTO subCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubCategory partially : {}, {}", id, subCategoryDTO);
        if (subCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubCategoryDTO> result = subCategoryService.partialUpdate(subCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-categories} : get all the subCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategories in body.
     */
    @GetMapping("/sub-categories")
    public List<SubCategoryDTO> getAllSubCategories() {
        log.debug("REST request to get all SubCategories");
        return subCategoryService.findAll();
    }

    /**
     * {@code GET  /sub-categories/:id} : get the "id" subCategory.
     *
     * @param id the id of the subCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategory(@PathVariable Long id) {
        log.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategoryDTO> subCategoryDTO = subCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategoryDTO);
    }

    /**
     * {@code DELETE  /sub-categories/:id} : delete the "id" subCategory.
     *
     * @param id the id of the subCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-categories/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Long id) {
        log.debug("REST request to delete SubCategory : {}", id);
        subCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

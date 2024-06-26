package com.binarybrothers.gymflexapi.controllers;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.binarybrothers.gymflexapi.dtos.category.CategoryDTO;
import com.binarybrothers.gymflexapi.entities.Category;
import com.binarybrothers.gymflexapi.entities.SubCategory;
import com.binarybrothers.gymflexapi.repositories.CategoryRepository;
import com.binarybrothers.gymflexapi.services.category.CategoryService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);
    private static final String ENTITY_NAME = "category";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.debug("REST request to save Category : {}", categoryDTO);
        validateCategoryCreation(categoryDTO);

        CategoryDTO result = categoryService.save(categoryDTO);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        log.debug("REST request to update Category : {}, {}", id, categoryDTO);
        validateCategoryUpdate(id, categoryDTO);

        CategoryDTO result = categoryService.update(categoryDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/categories/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<CategoryDTO> partialUpdateCategory(@PathVariable Long id, @NotNull @RequestBody CategoryDTO categoryDTO) {
        log.debug("REST request to partial update Category partially : {}, {}", id, categoryDTO);
        validateCategoryId(id, categoryDTO.getId());

        Optional<CategoryDTO> result = categoryService.partialUpdate(categoryDTO);
        return ResponseUtil.wrapOrNotFound(result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryDTO.getId().toString()));
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Optional<CategoryDTO> categoryDTO = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryDTO);
    }

    @DeleteMapping(value = "/categories/{id}", produces = "text/plain")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        checkCategoryForSubcategories(id);

        categoryService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    private void validateCategoryCreation(CategoryDTO categoryDTO) {
        if (categoryService.existsByName(categoryDTO.getName())) {
            throw new BadRequestAlertException("Category name is already used", ENTITY_NAME, "nameexists");
        }
        if (categoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new category must have a name", ENTITY_NAME, "namerequired");
        }
        if (categoryDTO.getIsForInventory() == null) {
            throw new BadRequestAlertException("A new category must have a IsForInventory", ENTITY_NAME, "IsForInventoryrequired");
        }
        if (categoryDTO.getIsForClient() == null) {
            throw new BadRequestAlertException("A new category must have a IsForClient", ENTITY_NAME, "IsForClientrequired");
        }
    }

    private void validateCategoryUpdate(Long id, CategoryDTO categoryDTO) {
        CategoryDTO oldCategory = categoryService.findOne(id).orElseThrow(() ->
                new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        if (!oldCategory.getName().equals(categoryDTO.getName()) && categoryService.existsByName(categoryDTO.getName())) {
            throw new BadRequestAlertException("Category name is already used", ENTITY_NAME, "nameexists");
        }
        if (!Objects.equals(id, categoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (categoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
    }

    private void validateCategoryId(Long pathId, Long dtoId) {
        if (dtoId == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(pathId, dtoId)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!categoryRepository.existsById(pathId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    private void checkCategoryForSubcategories(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        if (category.getSubCategoryList() != null && !category.getSubCategoryList().isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Cannot delete category with associated subcategories: ");
            for (SubCategory subCategory : category.getSubCategoryList()) {
                errorMessage.append(subCategory.getName()).append(", ");
            }
            errorMessage.setLength(errorMessage.length() - 2);  // Remove the last comma and space
            throw new BadRequestAlertException(errorMessage.toString(), ENTITY_NAME, "subcategoriesexist");
        }
    }
}
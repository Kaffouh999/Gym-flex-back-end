package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.dtos.subCategory.SubCategoryDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.entities.SubCategory;
import com.binarybrothers.gymflexapi.repositories.SubCategoryRepository;
import com.binarybrothers.gymflexapi.services.subCategory.SubCategoryService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoryResource.class);

    private static final String ENTITY_NAME = "subCategory";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final SubCategoryService subCategoryService;
    private final SubCategoryRepository subCategoryRepository;


    @PostMapping("/sub-categories")
    public ResponseEntity<Object> createSubCategory(@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save SubCategory : {}", subCategoryDTO);

        if (subCategoryService.existsByName(subCategoryDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("SubCategory name is already used");
        }

        if (subCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new subCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (subCategoryDTO.getName() == null || subCategoryDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new subCategory should have a name", ENTITY_NAME, "namerequired");
        }
        SubCategoryDTO result = subCategoryService.save(subCategoryDTO);
        return ResponseEntity
                .created(new URI("/api/sub-categories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/sub-categories/{id}")
    public ResponseEntity<Object> updateSubCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody SubCategoryDTO subCategoryDTO
    ) {
        log.debug("REST request to update SubCategory : {}, {}", id, subCategoryDTO);

        SubCategoryDTO oldSubCategory = subCategoryService.findOne(id).orElse(null);
        if (oldSubCategory != null && !oldSubCategory.getName().equals(subCategoryDTO.getName()) && subCategoryService.existsByName(subCategoryDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("SubCategory name is already used");
        }

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
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, subCategoryDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/sub-categories/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<SubCategoryDTO> partialUpdateSubCategory(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody SubCategoryDTO subCategoryDTO
    ) {
        log.debug("REST request to partial update SubCategory partially : {}, {}", id, subCategoryDTO);
        if (subCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id ", ENTITY_NAME, "idnull");
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
                HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, subCategoryDTO.getId().toString())
        );
    }

    @GetMapping("/sub-categories")
    public List<SubCategoryDTO> getAllSubCategories() {
        log.debug("REST request to get all SubCategories");
        return subCategoryService.findAll();
    }

    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategory(@PathVariable Long id) {
        log.debug("REST request to get SubCategory : {}", id);
        Optional<SubCategoryDTO> subCategoryDTO = subCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategoryDTO);
    }

    @DeleteMapping("/sub-categories/{id}")
    public ResponseEntity<Object> deleteSubCategory(@PathVariable Long id) {
        log.debug("REST request to delete SubCategory : {}", id);

        SubCategory subCategory = subCategoryRepository.findById(id).orElse(null);
        if (subCategory != null && subCategory.getEquipmentList() != null && !subCategory.getEquipmentList().isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Cannot delete sub-category with those associated equipments : ");
            for (Equipment equipment : subCategory.getEquipmentList()) {
                errorMessage.append(" -> ").append(equipment.getName());
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage.toString());
        }

        subCategoryService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString()))
                .build();
    }
}

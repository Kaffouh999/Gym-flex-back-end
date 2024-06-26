package com.binarybrothers.gymflexapi.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.binarybrothers.gymflexapi.dtos.equipment.EquipmentDTO;
import com.binarybrothers.gymflexapi.dtos.statistics.EquipmentStatisticsDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.repositories.EquipmentRepository;
import com.binarybrothers.gymflexapi.services.equipment.EquipmentService;
import com.binarybrothers.gymflexapi.services.mappers.EquipmentMapper;
import com.binarybrothers.gymflexapi.services.upload.IUploadService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller .
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final EquipmentRepository equipmentRepository;
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;
    private final IUploadService uploadService;


    /**
     * {@code POST  /equipment} : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentDTO,
     * or with status {@code 400 (Bad Request)} if the equipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment")
    public ResponseEntity<Object> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);

        if (equipmentService.existsByName(equipmentDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("equipment name is already used");
        }

        if (equipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (equipmentDTO.getName() == null || equipmentDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new equipment should have a name", ENTITY_NAME, "namerequired");
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /equipment/:id} : Updates an existing equipment.
     *
     * @param id           the id of the equipmentDTO to save.
     * @param equipmentDTO the equipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentDTO couldn't be updated.
     */
    @PutMapping("/equipment/{id}")
    public ResponseEntity<Object> updateEquipment(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EquipmentDTO equipmentDTO) {
        log.debug("REST request to update Equipment : {}, {}", id, equipmentDTO);

        EquipmentDTO oldEquipment = equipmentService.findOne(id).get();
        if (!oldEquipment.getName().equals(equipmentDTO.getName()) && equipmentService.existsByName(equipmentDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("equipment name is already used");
        }
        if (equipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EquipmentDTO result = equipmentService.update(equipmentDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, equipmentDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /equipment/:id} : Partial updates given fields of an existing equipment, field will ignore if it is null.
     *
     * @param id           the id of the equipmentDTO to save.
     * @param equipmentDTO the equipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipmentDTO couldn't be updated.
     */
    @PatchMapping(value = "/equipment/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<EquipmentDTO> partialUpdateEquipment(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody EquipmentDTO equipmentDTO) {
        log.debug("REST request to partial update Equipment partially : {}, {}", id, equipmentDTO);
        if (equipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquipmentDTO> result = equipmentService.partialUpdate(equipmentDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, equipmentDTO.getId().toString()));
    }

    /**
     * {@code GET  /equipment} : get all the equipment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipment in body
     */
    @GetMapping("/equipment")
    public List<EquipmentDTO> getAllEquipment() {
        log.debug("REST request to get all Equipment");
        return equipmentService.findAll();
    }

    /**
     * {@code GET  /equipment/statistics} : get statistics for all equipment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipment statistics in body.
     */
    @GetMapping("/equipment/statistics")
    public List<EquipmentStatisticsDTO> getEquipmentStatistic() {
        log.debug("REST request to get all Equipment statistics");
        return equipmentRepository.getEquipmentStatistics();
    }

    /**
     * {@code GET  /equipment/:id} : get the "id" equipment.
     *
     * @param id the id of the equipmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment/{id}")
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        Optional<EquipmentDTO> equipmentDTO = equipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentDTO);
    }

    /**
     * {@code DELETE  /equipment/:id} : delete the "id" equipment.
     *
     * @param id the id of the equipmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Object> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);

        Optional<Equipment> equipment = equipmentService.findById(id);
        if (equipment.isPresent() && equipment.get().getEquipmentItemList() != null && !equipment.get().getEquipmentItemList().isEmpty()) {
            String errorMessage = "Cannot delete equipment with associated equipment items.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }

        String folderUrl = "/images/equipments/";
        String urlImage = equipment.get().getImageUrl();
        uploadService.deleteDocument(folderUrl, urlImage);
        equipmentService.delete(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code POST  /equipment/upload/:name} : Handle file upload for equipment.
     *
     * @param name the name of the equipment.
     * @param file the file to upload.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file URL, or with status {@code 500 (Internal Server Error)} if there is an error during file upload.
     */
    @PostMapping("/equipment/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name, @RequestParam(value = "file", required = false) MultipartFile file) {
        String folderUrl = "/images/equipments/";
        Map<String, String> response = new HashMap<>();

        try {
            if (file != null) {
                String fileName = uploadService.handleFileUpload(name, folderUrl, file);
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                }
                response.put("message", "/images/equipments/" + fileName);
            } else {
                response.put("message", "");
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code PUT  /equipment/upload/:id} : Update file upload for equipment.
     *
     * @param id   the id of the equipment.
     * @param file the file to upload.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the file URL, or with status {@code 500 (Internal Server Error)} if there is an error during file upload.
     */
    @PutMapping("/equipment/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        Optional<Equipment> equipment = equipmentService.findById(id);
        String imageUrl = equipment.map(Equipment::getImageUrl).orElse(null);
        String folderUrl = "/images/equipments/";

        try {
            if (file != null) {
                uploadService.deleteDocument(folderUrl, imageUrl);
                String fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);

                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = equipment.get().getName();
                    fileName = uploadService.handleFileUpload(imageUrl, folderUrl, file);
                } else {
                    fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);
                }
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                } else {
                    equipment.get().setImageUrl(folderUrl + fileName);
                    equipmentService.save(equipmentMapper.toDto(equipment.get()));
                }
                response.put("message", folderUrl + fileName);

            } else {
                response.put("message", "");
            }
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

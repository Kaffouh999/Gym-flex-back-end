package com.binarybrothers.gymflexapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;
import com.binarybrothers.gymflexapi.repositories.EquipmentItemRepository;
import com.binarybrothers.gymflexapi.services.equipmentItem.EquipmentItemService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller .
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EquipmentItemResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentItemResource.class);
    private static final String ENTITY_NAME = "equipmentItem";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final EquipmentItemService equipmentItemService;
    private final EquipmentItemRepository equipmentItemRepository;


    /**
     * {@code POST  /equipment-items} : Create a new equipmentItem.
     *
     * @param equipmentItemDTO the equipmentItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentItemDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws NoSuchAlgorithmException if there is an error generating the ID.
     */
    @PostMapping("/equipment-items")
    public ResponseEntity<EquipmentItemDTO> createEquipmentItem(@Valid @RequestBody EquipmentItemDTO equipmentItemDTO)
            throws URISyntaxException, NoSuchAlgorithmException {
        log.debug("REST request to save EquipmentItem : {}", equipmentItemDTO);
        if (equipmentItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipmentItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipmentItemDTO result = equipmentItemService.save(equipmentItemDTO);
        return ResponseEntity
                .created(new URI("/api/equipment-items/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /equipment-items/:id} : Updates an existing equipmentItem.
     *
     * @param id the id of the equipmentItemDTO to save.
     * @param equipmentItemDTO the equipmentItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentItemDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentItemDTO couldn't be updated.
     */
    @PutMapping("/equipment-items/{id}")
    public ResponseEntity<EquipmentItemDTO> updateEquipmentItem(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody EquipmentItemDTO equipmentItemDTO) {
        log.debug("REST request to update EquipmentItem : {}, {}", id, equipmentItemDTO);
        if (equipmentItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipmentItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipmentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EquipmentItemDTO result = equipmentItemService.update(equipmentItemDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentItemDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /equipment-items/:id} : Partial updates given fields of an existing equipmentItem, field will ignore if it is null.
     *
     * @param id the id of the equipmentItemDTO to save.
     * @param equipmentItemDTO the equipmentItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentItemDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipmentItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipmentItemDTO couldn't be updated.
     */
    @PatchMapping(value = "/equipment-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EquipmentItemDTO> partialUpdateEquipmentItem(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody EquipmentItemDTO equipmentItemDTO) {
        log.debug("REST request to partial update EquipmentItem partially : {}, {}", id, equipmentItemDTO);
        if (equipmentItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipmentItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipmentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquipmentItemDTO> result = equipmentItemService.partialUpdate(equipmentItemDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equipment-items} : get all the equipmentItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipmentItems in body.
     */
    @GetMapping("/equipment-items")
    public List<EquipmentItemDTO> getAllEquipmentItems() {
        log.debug("REST request to get all EquipmentItems");
        return equipmentItemService.findAll();
    }

    /**
     * {@code GET  /equipment-items/:id} : get the "id" equipmentItem.
     *
     * @param id the id of the equipmentItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentItemDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-items/{id}")
    public ResponseEntity<EquipmentItemDTO> getEquipmentItem(@PathVariable Long id) {
        log.debug("REST request to get EquipmentItem : {}", id);
        Optional<EquipmentItemDTO> equipmentItemDTO = equipmentItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipmentItemDTO);
    }

    /**
     * {@code GET  /equipment-items/qrCode/:qrCode} : get the equipmentItem by QR code.
     *
     * @param qrCode the QR code of the equipmentItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipmentItemDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipment-items/qrCode/{qrCode}")
    public ResponseEntity<EquipmentItemDTO> getEquipmentItemByQrCode(@PathVariable String qrCode) {
        Optional<EquipmentItemDTO> equipmentItemDTO = equipmentItemService.findByBareCode(qrCode);
        return ResponseUtil.wrapOrNotFound(equipmentItemDTO);
    }

    /**
     * {@code DELETE  /equipment-items/:id} : delete the "id" equipmentItem.
     *
     * @param id the id of the equipmentItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO CONTENT)}.
     */
    @DeleteMapping("/equipment-items/{id}")
    public ResponseEntity<Void> deleteEquipmentItem(@PathVariable Long id) {
        log.debug("REST request to delete EquipmentItem : {}", id);
        equipmentItemService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}

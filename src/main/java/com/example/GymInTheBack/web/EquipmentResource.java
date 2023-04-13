package com.example.GymInTheBack.web;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.repositories.EquipmentRepository;
import com.example.GymInTheBack.services.equipment.EquipmentService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    private static final String ENTITY_NAME = "equipment";


    private String applicationName="GymFlex";

    private final EquipmentService equipmentService;

    private final EquipmentRepository equipmentRepository;

    public EquipmentResource(EquipmentService equipmentService, EquipmentRepository equipmentRepository) {
        this.equipmentService = equipmentService;
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * {@code POST  /equipment} : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipmentDTO, or with status {@code 400 (Bad Request)} if the equipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipment")
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (equipmentDTO.getName() == null || equipmentDTO.getName().trim().equals("")) {
            throw new BadRequestAlertException("A new equipment should have a name", ENTITY_NAME, "namerequired");
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity
            .created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipment/:id} : Updates an existing equipment.
     *
     * @param id the id of the equipmentDTO to save.
     * @param equipmentDTO the equipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipment/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EquipmentDTO equipmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}, {}", id, equipmentDTO);
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
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /equipment/:id} : Partial updates given fields of an existing equipment, field will ignore if it is null
     *
     * @param id the id of the equipmentDTO to save.
     * @param equipmentDTO the equipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipmentDTO,
     * or with status {@code 400 (Bad Request)} if the equipmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equipment/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EquipmentDTO> partialUpdateEquipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EquipmentDTO equipmentDTO
    ) throws URISyntaxException {
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

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equipmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equipment} : get all the equipment.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipment in body.
     */
    @GetMapping("/equipment")
    public List<EquipmentDTO> getAllEquipment() {
        log.debug("REST request to get all Equipment");
        return equipmentService.findAll();
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
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.deleteImage(id);
        equipmentService.delete(id);



        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/equipment/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name , @RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            File uploadDir = new File("C:\\youssef.com");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Path dest = uploadDir.toPath().resolve(name+"."+extension);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }


            Map<String, String> response = new HashMap<>();
            response.put("message", "http://localhost:5051/"+dest.getFileName().toString());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/equipment/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id , @RequestParam("file") MultipartFile file) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        equipmentService.deleteImage(id);


        int lastIndex = equipment.get().getImageUrl().lastIndexOf("/");
        String name = equipment.get().getImageUrl().substring(lastIndex + 1);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            File uploadDir = new File("/home/youssef/Documents/GYmFlexDocuments/images/equipments");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Path dest = uploadDir.toPath().resolve(name);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
            }


            Map<String, String> response = new HashMap<>();
            response.put("message", "http://localhost:5051/images/equipments/"+dest.getFileName().toString());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}

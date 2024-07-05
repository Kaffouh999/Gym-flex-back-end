package com.binarybrothers.gymflexapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.entities.GymBranch;
import com.binarybrothers.gymflexapi.repositories.GymBranchRepository;
import com.binarybrothers.gymflexapi.services.gymbranch.GymBranchService;
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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GymBranchController {

    private final Logger log = LoggerFactory.getLogger(GymBranchController.class);

    private static final String ENTITY_NAME = "gymBranch";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final GymBranchService gymBranchService;
    private final GymBranchRepository gymBranchRepository;


    /**
     * {@code POST  /gym-branches} : Create a new gymBranch.
     *
     * @param gymBranchDTO the gymBranchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gymBranchDTO,
     *         or with status {@code 400 (Bad Request)} if the gymBranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gym-branches")
    public ResponseEntity<Object> createGymBranch(@Valid @RequestBody GymBranchDTO gymBranchDTO) throws URISyntaxException {
        log.debug("REST request to save GymBranch : {}", gymBranchDTO);

        if (gymBranchService.existsByName(gymBranchDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Gym Branch name is already used");
        }
        validateNewGymBranch(gymBranchDTO);

        GymBranchDTO result = gymBranchService.save(gymBranchDTO);
        return ResponseEntity.created(new URI("/api/gym-branches/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    private void validateNewGymBranch(GymBranchDTO gymBranchDTO) {
        if (gymBranchDTO.getId() != null) {
            throw new BadRequestAlertException("A new gymBranch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (gymBranchDTO.getName() == null || gymBranchDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new gymBranch must have a name", ENTITY_NAME, "namerequired");
        }
        if (gymBranchDTO.getEmail() == null || gymBranchDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestAlertException("A new gymBranch must have an email", ENTITY_NAME, "emailrequired");
        }
        if (gymBranchDTO.getOpeningDate() == null) {
            throw new BadRequestAlertException("A new gymBranch must have an opening date", ENTITY_NAME, "openingdaterequired");
        }
        if (gymBranchDTO.getClosingDate() == null) {
            throw new BadRequestAlertException("A new gymBranch must have a closing date", ENTITY_NAME, "closingdaterequired");
        }
        if (gymBranchDTO.getSessionDurationAllowed() == null || gymBranchDTO.getSessionDurationAllowed() == 0) {
            throw new BadRequestAlertException("A new gymBranch must have a session duration", ENTITY_NAME, "sessiondurationrequired");
        }
    }

    /**
     * {@code PUT  /gym-branches/:id} : Updates an existing gymBranch.
     *
     * @param id           the id of the gymBranchDTO to save.
     * @param gymBranchDTO the gymBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gymBranchDTO,
     *         or with status {@code 400 (Bad Request)} if the gymBranchDTO is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the gymBranchDTO couldn't be updated.
     */
    @PutMapping("/gym-branches/{id}")
    public ResponseEntity<Object> updateGymBranch(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody GymBranchDTO gymBranchDTO) {
        log.debug("REST request to update GymBranch : {}, {}", id, gymBranchDTO);

        GymBranchDTO oldGym = gymBranchService.findOne(id).orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        if (!oldGym.getName().equals(gymBranchDTO.getName()) && gymBranchService.existsByName(gymBranchDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Gym Branch name is already used");
        }
        validateGymBranchId(id, gymBranchDTO);

        GymBranchDTO result = gymBranchService.update(gymBranchDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gymBranchDTO.getId().toString()))
                .body(result);
    }

    private void validateGymBranchId(Long id, GymBranchDTO gymBranchDTO) {
        if (gymBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gymBranchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!gymBranchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code PATCH  /gym-branches/:id} : Partial updates given fields of an existing gymBranch, field will ignore if it is null.
     *
     * @param id           the id of the gymBranchDTO to save.
     * @param gymBranchDTO the gymBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gymBranchDTO,
     *         or with status {@code 400 (Bad Request)} if the gymBranchDTO is not valid,
     *         or with status {@code 404 (Not Found)} if the gymBranchDTO is not found,
     *         or with status {@code 500 (Internal Server Error)} if the gymBranchDTO couldn't be updated.
     */
    @PatchMapping(value = "/gym-branches/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<GymBranchDTO> partialUpdateGymBranch(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody GymBranchDTO gymBranchDTO) {
        log.debug("REST request to partial update GymBranch partially : {}, {}", id, gymBranchDTO);

        validateGymBranchId(id, gymBranchDTO);

        Optional<GymBranchDTO> result = gymBranchService.partialUpdate(gymBranchDTO);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gymBranchDTO.getId().toString()));
    }

    /**
     * {@code GET  /gym-branches} : get all the gymBranches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gymBranches in body.
     */
    @GetMapping("/gym-branches")
    public List<GymBranchDTO> getAllGymBranches() {
        log.debug("REST request to get all GymBranches");
        return gymBranchService.findAll();
    }

    /**
     * {@code GET  /gym-branches/:id} : get the "id" gymBranch.
     *
     * @param id the id of the gymBranchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gymBranchDTO,
     *         or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gym-branches/{id}")
    public ResponseEntity<GymBranchDTO> getGymBranch(@PathVariable Long id) {
        log.debug("REST request to get GymBranch : {}", id);
        Optional<GymBranchDTO> gymBranchDTO = gymBranchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gymBranchDTO);
    }

    /**
     * {@code DELETE  /gym-branches/:id} : delete the "id" gymBranch.
     *
     * @param id the id of the gymBranchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gym-branches/{id}")
    public ResponseEntity<Object> deleteGymBranch(@PathVariable Long id) {
        log.debug("REST request to delete GymBranch : {}", id);

        GymBranch gymBranch = gymBranchRepository.findById(id).orElse(null);
        if (gymBranch != null && hasRelations(gymBranch)) {
            String errorMessage = "Cannot delete " + gymBranch.getName() + " Gym branch cause it still contains relations with members or equipment items or training sessions.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }

        gymBranchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    private boolean hasRelations(GymBranch gymBranch) {
        return (gymBranch.getMemberList() != null && !gymBranch.getMemberList().isEmpty()) ||
                (gymBranch.getEquipmentItemList() != null && !gymBranch.getEquipmentItemList().isEmpty()) ||
                (gymBranch.getSessionMemberList() != null && !gymBranch.getSessionMemberList().isEmpty());
    }
}

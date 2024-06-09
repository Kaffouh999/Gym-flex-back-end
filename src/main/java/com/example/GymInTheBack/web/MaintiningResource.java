package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.maintining.MaintiningDTO;
import com.example.GymInTheBack.repositories.MaintiningRepository;
import com.example.GymInTheBack.services.maintining.MaintiningService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MaintiningResource {

    private final Logger log = LoggerFactory.getLogger(MaintiningResource.class);

    private static final String ENTITY_NAME = "maintining";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final MaintiningService maintiningService;

    private final MaintiningRepository maintiningRepository;

    public MaintiningResource(MaintiningService maintiningService, MaintiningRepository maintiningRepository) {
        this.maintiningService = maintiningService;
        this.maintiningRepository = maintiningRepository;
    }

    /**
     * {@code POST  /maintinings} : Create a new maintining.
     *
     * @param maintiningDTO the maintiningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintiningDTO, or with status {@code 400 (Bad Request)} if the maintining has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maintinings")
    public ResponseEntity<Object> createMaintining(@Valid @RequestBody MaintiningDTO maintiningDTO) throws URISyntaxException {
        log.debug("REST request to save Maintining : {}", maintiningDTO);
        if (maintiningDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintining cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (maintiningDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new maintining should have a start date", ENTITY_NAME, "startDateRequired");
        }
        MaintiningDTO result = maintiningService.save(maintiningDTO);
        if (result != null) {
            return ResponseEntity.created(new URI("/api/maintinings/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
        } else {
            String errorMessage = "this equipment item is already gone to maintient in this periode you specified";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    /**
     * {@code PUT  /maintinings/:id} : Updates an existing maintining.
     *
     * @param id            the id of the maintiningDTO to save.
     * @param maintiningDTO the maintiningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintiningDTO,
     * or with status {@code 400 (Bad Request)} if the maintiningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintiningDTO couldn't be updated.
     */
    @PutMapping("/maintinings/{id}")
    public ResponseEntity<Object> updateMaintining(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody MaintiningDTO maintiningDTO) {
        log.debug("REST request to update Maintining : {}, {}", id, maintiningDTO);
        if (maintiningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintiningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintiningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        MaintiningDTO result = maintiningService.save(maintiningDTO);
        if (result != null) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, maintiningDTO.getId().toString())).body(result);
        } else {
            String errorMessage = "this equipment item is already gone to maintient in this periode you specified";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    /**
     * {@code PATCH  /maintinings/:id} : Partial updates given fields of an existing maintining, field will ignore if it is null
     *
     * @param id            the id of the maintiningDTO to save.
     * @param maintiningDTO the maintiningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintiningDTO,
     * or with status {@code 400 (Bad Request)} if the maintiningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintiningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintiningDTO couldn't be updated.
      */
    @PatchMapping(value = "/maintinings/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<MaintiningDTO> partialUpdateMaintining(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody MaintiningDTO maintiningDTO) {
        log.debug("REST request to partial update Maintining partially : {}, {}", id, maintiningDTO);
        if (maintiningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintiningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintiningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintiningDTO> result = maintiningService.partialUpdate(maintiningDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, maintiningDTO.getId().toString()));
    }

    /**
     * {@code GET  /maintinings} : get all the maintinings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintinings in body.
     */
    @GetMapping("/maintinings")
    public List<MaintiningDTO> getAllMaintinings() {
        log.debug("REST request to get all Maintinings");
        return maintiningService.findAll();
    }

    /**
     * {@code GET  /maintinings/:id} : get the "id" maintining.
     *
     * @param id the id of the maintiningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintiningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintinings/{id}")
    public ResponseEntity<MaintiningDTO> getMaintining(@PathVariable Long id) {
        log.debug("REST request to get Maintining : {}", id);
        Optional<MaintiningDTO> maintiningDTO = maintiningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintiningDTO);
    }

    /**
     * {@code DELETE  /maintinings/:id} : delete the "id" maintining.
     *
     * @param id the id of the maintiningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maintinings/{id}")
    public ResponseEntity<Void> deleteMaintining(@PathVariable Long id) {
        log.debug("REST request to delete Maintining : {}", id);
        maintiningService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}

package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.maintining.MaintiningDTO;
import com.example.GymInTheBack.dtos.reform.ReformDTO;
import com.example.GymInTheBack.repositories.ReformRepository;
import com.example.GymInTheBack.services.reform.ReformService;
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
public class ReformResource {

    private final Logger log = LoggerFactory.getLogger(ReformResource.class);

    private static final String ENTITY_NAME = "reform";


    private String applicationName="GymFlex";

    private final ReformService reformService;

    private final ReformRepository reformRepository;

    public ReformResource(ReformService reformService, ReformRepository reformRepository) {
        this.reformService = reformService;
        this.reformRepository = reformRepository;
    }

    /**
     * {@code POST  /reforms} : Create a new reform.
     *
     * @param reformDTO the reformDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reformDTO, or with status {@code 400 (Bad Request)} if the reform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reforms")
    public ResponseEntity<Object> createReform(@Valid @RequestBody ReformDTO reformDTO) throws URISyntaxException {
        log.debug("REST request to save Reform : {}", reformDTO);
        if (reformDTO.getId() != null) {
            throw new BadRequestAlertException("A new reform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReformDTO result = reformService.save(reformDTO);
        if(result != null) {
            return ResponseEntity
                    .created(new URI("/api/reforms/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
        }else{
            String errorMessage = "this equipment item is already reformed";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    /**
     * {@code PUT  /reforms/:id} : Updates an existing reform.
     *
     * @param id the id of the reformDTO to save.
     * @param reformDTO the reformDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reformDTO,
     * or with status {@code 400 (Bad Request)} if the reformDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reformDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reforms/{id}")
    public ResponseEntity<Object> updateReform(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReformDTO reformDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reform : {}, {}", id, reformDTO);
        if (reformDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reformDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReformDTO result = reformService.update(reformDTO);
        if(result != null) {
            return ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reformDTO.getId().toString()))
                    .body(result);
        }else{
            String errorMessage = "this equipment item is already gone to reformed";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    /**
     * {@code PATCH  /reforms/:id} : Partial updates given fields of an existing reform, field will ignore if it is null
     *
     * @param id the id of the reformDTO to save.
     * @param reformDTO the reformDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reformDTO,
     * or with status {@code 400 (Bad Request)} if the reformDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reformDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reformDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reforms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReformDTO> partialUpdateReform(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReformDTO reformDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reform partially : {}, {}", id, reformDTO);
        if (reformDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reformDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReformDTO> result = reformService.partialUpdate(reformDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reformDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reforms} : get all the reforms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reforms in body.
     */
    @GetMapping("/reforms")
    public List<ReformDTO> getAllReforms() {
        log.debug("REST request to get all Reforms");
        return reformService.findAll();
    }

    /**
     * {@code GET  /reforms/:id} : get the "id" reform.
     *
     * @param id the id of the reformDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reformDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reforms/{id}")
    public ResponseEntity<ReformDTO> getReform(@PathVariable Long id) {
        log.debug("REST request to get Reform : {}", id);
        Optional<ReformDTO> reformDTO = reformService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reformDTO);
    }

    /**
     * {@code DELETE  /reforms/:id} : delete the "id" reform.
     *
     * @param id the id of the reformDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reforms/{id}")
    public ResponseEntity<Void> deleteReform(@PathVariable Long id) {
        log.debug("REST request to delete Reform : {}", id);
        reformService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

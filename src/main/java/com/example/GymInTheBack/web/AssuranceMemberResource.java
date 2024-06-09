package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.assuranceMember.AssuranceMemberDTO;
import com.example.GymInTheBack.repositories.AssuranceMemberRepository;
import com.example.GymInTheBack.services.assuranceMember.AssuranceMemberService;
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
public class AssuranceMemberResource {

    private final Logger log = LoggerFactory.getLogger(AssuranceMemberResource.class);

    private static final String ENTITY_NAME = "assuranceMember";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final AssuranceMemberService assuranceMemberService;

    private final AssuranceMemberRepository assuranceMemberRepository;

    public AssuranceMemberResource(AssuranceMemberService assuranceMemberService, AssuranceMemberRepository assuranceMemberRepository) {
        this.assuranceMemberService = assuranceMemberService;
        this.assuranceMemberRepository = assuranceMemberRepository;
    }

    /**
     * {@code POST  /assurance-members} : Create a new assuranceMember.
     *
     * @param assuranceMemberDTO the assuranceMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assuranceMemberDTO, or with status {@code 400 (Bad Request)} if the assuranceMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assurance-members")
    public ResponseEntity<Object> createAssuranceMember(@Valid @RequestBody AssuranceMemberDTO assuranceMemberDTO) throws URISyntaxException {
        log.debug("REST request to save AssuranceMember : {}", assuranceMemberDTO);
        if (assuranceMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new assuranceMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (assuranceMemberDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new assuranceMember must have start date", ENTITY_NAME, "startdaterequired");
        }
        if (assuranceMemberDTO.getEndDate() == null) {
            throw new BadRequestAlertException("A new assuranceMember  must have end date", ENTITY_NAME, "enddaterequired");
        }
        if (assuranceMemberDTO.getAmountPayed() == null) {
            throw new BadRequestAlertException("A new assuranceMember must have amount payed ", ENTITY_NAME, "amountpayedrequired");
        }

        AssuranceMemberDTO result = assuranceMemberService.save(assuranceMemberDTO);
        if (result != null) {
            return ResponseEntity.created(new URI("/api/assurance-members/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
        } else {
            String errorMessage = "the period of time you specified is already covered by an assurance for the user selected";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);

        }
    }

    /**
     * {@code PUT  /assurance-members/:id} : Updates an existing assuranceMember.
     *
     * @param id                 the id of the assuranceMemberDTO to save.
     * @param assuranceMemberDTO the assuranceMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assuranceMemberDTO,
     * or with status {@code 400 (Bad Request)} if the assuranceMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assuranceMemberDTO couldn't be updated.
     */
    @PutMapping("/assurance-members/{id}")
    public ResponseEntity<Object> updateAssuranceMember(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("REST request to update AssuranceMember : {}, {}", id, assuranceMemberDTO);
        if (assuranceMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assuranceMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuranceMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssuranceMemberDTO result = assuranceMemberService.update(assuranceMemberDTO);
        if (result != null) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, assuranceMemberDTO.getId().toString())).body(result);
        } else {
            String errorMessage = "the period of time you specified is already covered by an assurance for the user selected";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    /**
     * {@code PATCH  /assurance-members/:id} : Partial updates given fields of an existing assuranceMember, field will ignore if it is null
     *
     * @param id                 the id of the assuranceMemberDTO to save.
     * @param assuranceMemberDTO the assuranceMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assuranceMemberDTO,
     * or with status {@code 400 (Bad Request)} if the assuranceMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assuranceMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assuranceMemberDTO couldn't be updated.
     */
    @PatchMapping(value = "/assurance-members/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<AssuranceMemberDTO> partialUpdateAssuranceMember(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("REST request to partial update AssuranceMember partially : {}, {}", id, assuranceMemberDTO);
        if (assuranceMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assuranceMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuranceMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssuranceMemberDTO> result = assuranceMemberService.partialUpdate(assuranceMemberDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, assuranceMemberDTO.getId().toString()));
    }

    /**
     * {@code GET  /assurance-members} : get all the assuranceMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assuranceMembers in body.
     */
    @GetMapping("/assurance-members")
    public List<AssuranceMemberDTO> getAllAssuranceMembers() {
        log.debug("REST request to get all AssuranceMembers");
        return assuranceMemberService.findAll();
    }

    /**
     * {@code GET  /assurance-members/:id} : get the "id" assuranceMember.
     *
     * @param id the id of the assuranceMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assuranceMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assurance-members/{id}")
    public ResponseEntity<AssuranceMemberDTO> getAssuranceMember(@PathVariable Long id) {
        log.debug("REST request to get AssuranceMember : {}", id);
        Optional<AssuranceMemberDTO> assuranceMemberDTO = assuranceMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assuranceMemberDTO);
    }

    /**
     * {@code DELETE  /assurance-members/:id} : delete the "id" assuranceMember.
     *
     * @param id the id of the assuranceMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assurance-members/{id}")
    public ResponseEntity<Void> deleteAssuranceMember(@PathVariable Long id) {
        log.debug("REST request to delete AssuranceMember : {}", id);
        assuranceMemberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}

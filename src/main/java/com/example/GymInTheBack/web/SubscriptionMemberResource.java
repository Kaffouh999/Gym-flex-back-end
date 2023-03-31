package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.repositories.SubscriptionMemberRepository;
import com.example.GymInTheBack.services.subscriptionMember.SubscriptionMemberService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class SubscriptionMemberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionMemberResource.class);

    private static final String ENTITY_NAME = "subscriptionMember";


    private String applicationName = "GymFlex";

    private final SubscriptionMemberService subscriptionMemberService;

    private final SubscriptionMemberRepository subscriptionMemberRepository;

    public SubscriptionMemberResource(
        SubscriptionMemberService subscriptionMemberService,
        SubscriptionMemberRepository subscriptionMemberRepository
    ) {
        this.subscriptionMemberService = subscriptionMemberService;
        this.subscriptionMemberRepository = subscriptionMemberRepository;
    }

    /**
     * {@code POST  /subscription-members} : Create a new subscriptionMember.
     *
     * @param subscriptionMemberDTO the subscriptionMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionMemberDTO, or with status {@code 400 (Bad Request)} if the subscriptionMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-members")
    public ResponseEntity<SubscriptionMemberDTO> createSubscriptionMember(@Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionMember : {}", subscriptionMemberDTO);
        if (subscriptionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (subscriptionMemberDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new subscriptionMember must have date start", ENTITY_NAME, "datestartresuired");
        }
        SubscriptionMemberDTO result = subscriptionMemberService.save(subscriptionMemberDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-members/:id} : Updates an existing subscriptionMember.
     *
     * @param id the id of the subscriptionMemberDTO to save.
     * @param subscriptionMemberDTO the subscriptionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-members/{id}")
    public ResponseEntity<SubscriptionMemberDTO> updateSubscriptionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionMember : {}, {}", id, subscriptionMemberDTO);
        if (subscriptionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionMemberDTO result = subscriptionMemberService.update(subscriptionMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscription-members/:id} : Partial updates given fields of an existing subscriptionMember, field will ignore if it is null
     *
     * @param id the id of the subscriptionMemberDTO to save.
     * @param subscriptionMemberDTO the subscriptionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionMemberDTO> partialUpdateSubscriptionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionMember partially : {}, {}", id, subscriptionMemberDTO);
        if (subscriptionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionMemberDTO> result = subscriptionMemberService.partialUpdate(subscriptionMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-members} : get all the subscriptionMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionMembers in body.
     */
    @GetMapping("/subscription-members")
    public List<SubscriptionMemberDTO> getAllSubscriptionMembers() {
        log.debug("REST request to get all SubscriptionMembers");
        return subscriptionMemberService.findAll();
    }

    /**
     * {@code GET  /subscription-members/:id} : get the "id" subscriptionMember.
     *
     * @param id the id of the subscriptionMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-members/{id}")
    public ResponseEntity<SubscriptionMemberDTO> getSubscriptionMember(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionMember : {}", id);
        Optional<SubscriptionMemberDTO> subscriptionMemberDTO = subscriptionMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionMemberDTO);
    }

    /**
     * {@code DELETE  /subscription-members/:id} : delete the "id" subscriptionMember.
     *
     * @param id the id of the subscriptionMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-members/{id}")
    public ResponseEntity<Void> deleteSubscriptionMember(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionMember : {}", id);
        subscriptionMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

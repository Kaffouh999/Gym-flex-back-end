package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.example.GymInTheBack.entities.Payment;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SubscriptionMemberRepository;
import com.example.GymInTheBack.services.subscriptionMember.SubscriptionMemberService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SubscriptionMemberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionMemberResource.class);

    private static final String ENTITY_NAME = "subscriptionMember";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

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
    public ResponseEntity<SubscriptionMemberDTO> createSubscriptionMember(@Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO) throws NoSuchAlgorithmException, URISyntaxException, WriterException {
        log.debug("REST request to save SubscriptionMember : {}", subscriptionMemberDTO);
        if (subscriptionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (subscriptionMemberDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new subscriptionMember must have date start", ENTITY_NAME, "datestartresuired");
        }
        if (subscriptionMemberDTO.getPlan() == null) {
            throw new BadRequestAlertException("A new subscriptionMember must have plan", ENTITY_NAME, "planresuired");
        }


        SubscriptionMemberDTO result = subscriptionMemberService.save(subscriptionMemberDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
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
     */
    @PutMapping("/subscription-members/{id}")
    public ResponseEntity<SubscriptionMemberDTO> updateSubscriptionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) {
        log.debug("REST request to update SubscriptionMember : {}, {}", id, subscriptionMemberDTO);
        if (assertSubscriptionMemberIsNotNull(subscriptionMemberDTO)) {
            throw new BadRequestAlertException("Invalid id ", ENTITY_NAME, "idnull");
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
            .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, subscriptionMemberDTO.getId().toString()))
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
     */
    @PatchMapping(value = "/subscription-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionMemberDTO> partialUpdateSubscriptionMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) {
        log.debug("REST request to partial update SubscriptionMember partially : {}, {}", id, subscriptionMemberDTO);
        if (assertSubscriptionMemberIsNotNull(subscriptionMemberDTO)) {
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
            HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, subscriptionMemberDTO.getId().toString())
        );
    }

    public boolean assertSubscriptionMemberIsNotNull(SubscriptionMemberDTO subscriptionMemberDTO){
        return subscriptionMemberDTO != null && subscriptionMemberDTO.getId() != null;
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

    @GetMapping("/web/subscription-members/{id}")
    public List<SubscriptionWithPaymentsDTO> getSubscriptionMemberByMember(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionMember By Member : {}", id);
        return subscriptionMemberService.findByMemberId(id);
    }
    @GetMapping("/subscription-members/qrcode/{qrCode}")
    public ResponseEntity<SubscriptionMemberDTO> getSubscriptionMember(@PathVariable String qrCode) {
        log.debug("REST request to get SubscriptionMember by qrcode : {}", qrCode);
        Optional<SubscriptionMemberDTO> subscriptionMemberDTO = subscriptionMemberService.findByCodeSubscription(qrCode);
        return ResponseUtil.wrapOrNotFound(subscriptionMemberDTO);
    }

    /**
     * {@code DELETE  /subscription-members/:id} : delete the "id" subscriptionMember.
     *
     * @param id the id of the subscriptionMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-members/{id}")
    public ResponseEntity<Object> deleteSubscriptionMember(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionMember : {}", id);

        SubscriptionMember subscriptionMember = subscriptionMemberRepository.findById(id).orElse(null);
        if(subscriptionMember != null &&subscriptionMember.getPaymentList() != null && !subscriptionMember.getPaymentList().isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringBuilder errorMessage = new StringBuilder("Cannot delete subscription with those associated payments at : ");
            for(Payment payment : subscriptionMember.getPaymentList()){
                errorMessage.append(" -> ").append(payment.getPaymentDate().format(formatter));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage.toString());
        }

        subscriptionMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/subscription-members/search")
    public ResponseEntity<List<SubscriptionMemberDTO>> searchSubs(
        @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) {
        // Logic to search the data based on the provided search criteria
        List<SubscriptionMemberDTO> searchData = subscriptionMemberService.searchSubs(subscriptionMemberDTO);

        return ResponseEntity.ok(searchData);
    }
}

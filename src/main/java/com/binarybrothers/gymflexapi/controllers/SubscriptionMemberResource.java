package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.binarybrothers.gymflexapi.entities.Payment;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.binarybrothers.gymflexapi.repositories.SubscriptionMemberRepository;
import com.binarybrothers.gymflexapi.services.subscriptionMember.SubscriptionMemberService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import com.google.zxing.WriterException;
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
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscriptionMemberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionMemberResource.class);

    private static final String ENTITY_NAME = "subscriptionMember";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final SubscriptionMemberService subscriptionMemberService;
    private final SubscriptionMemberRepository subscriptionMemberRepository;


    @PostMapping("/subscription-members")
    public ResponseEntity<SubscriptionMemberDTO> createSubscriptionMember(@Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO) throws NoSuchAlgorithmException, URISyntaxException, WriterException {
        log.debug("REST request to save SubscriptionMember : {}", subscriptionMemberDTO);
        if (subscriptionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (subscriptionMemberDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new subscriptionMember must have date start", ENTITY_NAME, "datestartrequired");
        }
        if (subscriptionMemberDTO.getPlan() == null) {
            throw new BadRequestAlertException("A new subscriptionMember must have plan", ENTITY_NAME, "planrequired");
        }

        SubscriptionMemberDTO result = subscriptionMemberService.save(subscriptionMemberDTO);
        return ResponseEntity
                .created(new URI("/api/subscription-members/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/subscription-members/{id}")
    public ResponseEntity<SubscriptionMemberDTO> updateSubscriptionMember(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody SubscriptionMemberDTO subscriptionMemberDTO
    ) {
        log.debug("REST request to update SubscriptionMember : {}, {}", id, subscriptionMemberDTO);
        if (assertSubscriptionMemberIsNotNull(subscriptionMemberDTO)) {
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
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, subscriptionMemberDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/subscription-members/{id}", consumes = {"application/json", "application/merge-patch+json"})
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

    public boolean assertSubscriptionMemberIsNotNull(SubscriptionMemberDTO subscriptionMemberDTO) {
        return subscriptionMemberDTO != null && subscriptionMemberDTO.getId() != null;
    }

    @GetMapping("/subscription-members")
    public List<SubscriptionMemberDTO> getAllSubscriptionMembers() {
        log.debug("REST request to get all SubscriptionMembers");
        return subscriptionMemberService.findAll();
    }

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
    public ResponseEntity<SubscriptionMemberDTO> getSubscriptionMemberByQrCode(@PathVariable String qrCode) {
        log.debug("REST request to get SubscriptionMember by qrcode : {}", qrCode);
        Optional<SubscriptionMemberDTO> subscriptionMemberDTO = subscriptionMemberService.findByCodeSubscription(qrCode);
        return ResponseUtil.wrapOrNotFound(subscriptionMemberDTO);
    }

    @DeleteMapping("/subscription-members/{id}")
    public ResponseEntity<Object> deleteSubscriptionMember(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionMember : {}", id);

        SubscriptionMember subscriptionMember = subscriptionMemberRepository.findById(id).orElse(null);
        if (subscriptionMember != null && subscriptionMember.getPaymentList() != null && !subscriptionMember.getPaymentList().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringBuilder errorMessage = new StringBuilder("Cannot delete subscription with those associated payments at : ");
            for (Payment payment : subscriptionMember.getPaymentList()) {
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

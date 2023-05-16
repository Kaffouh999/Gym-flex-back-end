package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.payment.PaymentDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.repositories.PaymentRepository;
import com.example.GymInTheBack.services.payment.PaymentService;
import com.example.GymInTheBack.services.subscriptionMember.SubscriptionMemberService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "payment";


    private String applicationName ="GymFlex";

    private final PaymentService paymentService;

    private final SubscriptionMemberService subscriptionMemberService;
    private final PaymentRepository paymentRepository;

    public PaymentResource(PaymentService paymentService, SubscriptionMemberService subscriptionMemberService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.subscriptionMemberService = subscriptionMemberService;
        this.paymentRepository = paymentRepository;
    }

    /**
     * {@code POST  /payments} : Create a new payment.
     *
     * @param paymentDTO the paymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentDTO, or with status {@code 400 (Bad Request)} if the payment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) throws URISyntaxException, NoSuchAlgorithmException, WriterException {
        log.debug("REST request to save Payment : {}", paymentDTO);
        if (paymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (paymentDTO.getPaymentDate() == null) {
            throw new BadRequestAlertException("A new payment should have paymentDate", ENTITY_NAME, "paymentdaterequired");
        }
        if (paymentDTO.getAmountPayed() == null) {
            throw new BadRequestAlertException("A new payment should have  ammountpayed", ENTITY_NAME, "amountpayedrequired");
        }
        if (paymentDTO.getSubscriptionMember() == null || paymentDTO.getSubscriptionMember().getId() == null ) {
            throw new BadRequestAlertException("A new payment should have  subscription", ENTITY_NAME, "subscriptionrequired");
        }

        ZonedDateTime dateNow = ZonedDateTime.now();
        SubscriptionMemberDTO subscriptionMemberDTO = paymentDTO.getSubscriptionMember();
        if(subscriptionMemberDTO.getEndDate() != null ) {

            if(dateNow.isAfter( subscriptionMemberDTO.getEndDate() )){
                subscriptionMemberDTO.setStartDate(dateNow);
                ZonedDateTime endDate = dateNow.plusDays(subscriptionMemberDTO.getPlan().getDuration());
                subscriptionMemberDTO.setEndDate(endDate);
            }
            else{
                ZonedDateTime endDate = subscriptionMemberDTO.getEndDate().plusDays(subscriptionMemberDTO.getPlan().getDuration());
                subscriptionMemberDTO.setEndDate(endDate);
            }

        }else{
            ZonedDateTime endDate = subscriptionMemberDTO.getStartDate().plusDays(subscriptionMemberDTO.getPlan().getDuration());
            subscriptionMemberDTO.setEndDate(endDate);
        }

        subscriptionMemberService.save(subscriptionMemberDTO);

        PaymentDTO result = paymentService.save(paymentDTO);
        return ResponseEntity
            .created(new URI("/api/payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payments/:id} : Updates an existing payment.
     *
     * @param id the id of the paymentDTO to save.
     * @param paymentDTO the paymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentDTO paymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Payment : {}, {}", id, paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentDTO result = paymentService.update(paymentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payments/:id} : Partial updates given fields of an existing payment, field will ignore if it is null
     *
     * @param id the id of the paymentDTO to save.
     * @param paymentDTO the paymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentDTO> partialUpdatePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentDTO paymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Payment partially : {}, {}", id, paymentDTO);
        if (paymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentDTO> result = paymentService.partialUpdate(paymentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payments} : get all the payments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payments in body.
     */
    @GetMapping("/payments")
    public List<PaymentDTO> getAllPayments() {
        log.debug("REST request to get all Payments");
        return paymentService.findAll();
    }

    /**
     * {@code GET  /payments/:id} : get the "id" payment.
     *
     * @param id the id of the paymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> paymentDTO = paymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDTO);
    }

    @GetMapping("/web/payments/{id}")
    public List<PaymentDTO> getPaymentByMember(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        return paymentService.findAll();
    }
    /**
     * {@code DELETE  /payments/:id} : delete the "id" payment.
     *
     * @param id the id of the paymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payments/{id}")
            public ResponseEntity<Object> deletePayment(@PathVariable Long id) throws NoSuchAlgorithmException, WriterException {
        log.debug("REST request to delete Payment : {}", id);

        PaymentDTO paymentDTO = paymentService.findOne(id).get();
        Long planDuration = paymentDTO.getSubscriptionMember().getPlan().getDuration();

        SubscriptionMemberDTO subscriptionMemberDTO = paymentDTO.getSubscriptionMember();
        ZonedDateTime dateEnd = subscriptionMemberDTO.getEndDate();
        ZonedDateTime dateNow = ZonedDateTime.now();
        ZonedDateTime updatedDate;
        long differenceDays = ChronoUnit.DAYS.between(dateNow, dateEnd);
        if(dateEnd.isAfter( dateNow ) && differenceDays>0){

            if(differenceDays > planDuration){
                updatedDate= dateEnd.minusDays(planDuration);
            }
            else{
                 updatedDate= dateEnd.minusDays(differenceDays);
            }

        }else{
            String errorMessage = "Cannot delete this payment , it's already consumed ";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
        subscriptionMemberDTO.setEndDate(updatedDate);
        subscriptionMemberService.save(subscriptionMemberDTO);
        paymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

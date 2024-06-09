package com.example.GymInTheBack.web;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.plan.PlanDTO;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.PlanRepository;
import com.example.GymInTheBack.services.mappers.PlanMapper;
import com.example.GymInTheBack.services.plan.PlanService;
import com.example.GymInTheBack.services.upload.UploadService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PlanResource {

    private final Logger log = LoggerFactory.getLogger(PlanResource.class);

    private static final String ENTITY_NAME = "plan";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final PlanService planService;
    private final UploadService uploadService;
    private final PlanRepository planRepository;

    public PlanResource(PlanService planService, UploadService uploadService, PlanRepository planRepository) {
        this.planService = planService;
        this.uploadService = uploadService;
        this.planRepository = planRepository;
    }

    /**
     * {@code POST  /plans} : Create a new plan.
     *
     * @param planDTO the planDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planDTO, or with status {@code 400 (Bad Request)} if the plan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plans")
    public ResponseEntity<Object> createPlan(@Valid @RequestBody PlanDTO planDTO) throws URISyntaxException {
        log.debug("REST request to save Plan : {}", planDTO);
        if (planService.existsByName(planDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Plan name is already used");
        }
        if (planDTO.getId() != null) {
            throw new BadRequestAlertException("A new plan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (planDTO.getDuration() == null || planDTO.getDuration() <= 0) {
            throw new BadRequestAlertException("A new plan must have duration and it must be positive", ENTITY_NAME, "durationrequired");
        }
        if (planDTO.getPrice() == null) {
            throw new BadRequestAlertException("A new plan must have price", ENTITY_NAME, "pricerequired");
        }
        if (planDTO.getName() == null || planDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new plan must have name", ENTITY_NAME, "namerequired");
        }
        PlanDTO result = planService.save(planDTO);
        return ResponseEntity.created(new URI("/api/plans/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /plans/:id} : Updates an existing plan.
     *
     * @param id      the id of the planDTO to save.
     * @param planDTO the planDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planDTO,
     * or with status {@code 400 (Bad Request)} if the planDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planDTO couldn't be updated.
     */
    @PutMapping("/plans/{id}")
    public ResponseEntity<Object> updatePlan(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PlanDTO planDTO) {
        log.debug("REST request to update Plan : {}, {}", id, planDTO);

        PlanDTO oldPlan = planService.findOne(id).get();
        if (!oldPlan.getName().equals(planDTO.getName()) && planService.existsByName(planDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Plan name is already used");
        }
        if (planDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlanDTO result = planService.update(planDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, planDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /plans/:id} : Partial updates given fields of an existing plan, field will ignore if it is null
     *
     * @param id      the id of the planDTO to save.
     * @param planDTO the planDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planDTO,
     * or with status {@code 400 (Bad Request)} if the planDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planDTO couldn't be updated.
     */
    @PatchMapping(value = "/plans/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<PlanDTO> partialUpdatePlan(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody PlanDTO planDTO) {
        log.debug("REST request to partial update Plan partially : {}, {}", id, planDTO);
        if (planDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanDTO> result = planService.partialUpdate(planDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, planDTO.getId().toString()));
    }

    /**
     * {@code GET  /plans} : get all the plans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plans in body.
     */
    @GetMapping("/plans")
    public List<PlanDTO> getAllPlans() {
        log.debug("REST request to get all Plans");
        return planService.findAll();
    }

    /**
     * {@code GET  /plans/:id} : get the "id" plan.
     *
     * @param id the id of the planDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plans/{id}")
    public ResponseEntity<PlanDTO> getPlan(@PathVariable Long id) {
        log.debug("REST request to get Plan : {}", id);
        Optional<PlanDTO> planDTO = planService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planDTO);
    }

    /**
     * {@code DELETE  /plans/:id} : delete the "id" plan.
     *
     * @param id the id of the planDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Object> deletePlan(@PathVariable Long id) {
        log.debug("REST request to delete Plan : {}", id);
        Plan plan = planRepository.findById(id).orElse(null);

        if (plan != null && plan.getSubscriptionMemberList() != null && !plan.getSubscriptionMemberList().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            StringBuilder errorMessage = new StringBuilder("Cannot delete plan with those associated Subscriptions  : ");
            for (SubscriptionMember subscriptionMember : plan.getSubscriptionMemberList()) {
                errorMessage.append(" (").append(subscriptionMember.getMember().getOnlineUser().getFirstName()).append(" ").append(subscriptionMember.getStartDate().format(formatter)).append(" ").append(subscriptionMember.getPlan().getName()).append(")");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage.toString());
        }


        if (plan != null) {
            String folderUrl = "/images/plans/";
            String urlImage = plan.getImageAds();
            uploadService.deleteDocument(folderUrl, urlImage);
        }

        planService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }


    @PostMapping("/plans/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name, @RequestParam(value = "file", required = false) MultipartFile file) {
        String folerUrl = "/images/plans/";
        Map<String, String> response = new HashMap<>();

        try {
            if (file != null) {
                String fileName = uploadService.handleFileUpload(name, folerUrl, file);
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                }
                response.put("message", "http://localhost:5051/images/plans/" + fileName);
            } else {
                response.put("message", "");
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/plans/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        Optional<PlanDTO> plan = planService.findOne(id);
        String imageUrl = plan.get().getImageAds();
        String folderUrl = "/images/plans/";

        try {
            if (file != null) {
                uploadService.deleteDocument(folderUrl, imageUrl);
                String fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);

                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = plan.get().getName();
                    fileName = uploadService.handleFileUpload(imageUrl, folderUrl, file);
                } else {
                    fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);
                }
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                } else {
                    plan.get().setImageAds("http://localhost:5051" + folderUrl + fileName);
                    planService.save(plan.get());
                }
                response.put("message", "http://localhost:5051" + folderUrl + fileName);

            } else {
                response.put("message", "");
            }
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

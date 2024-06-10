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
public class AssuranceMemberResource {

    private final Logger log = LoggerFactory.getLogger(AssuranceMemberResource.class);
    private static final String ENTITY_NAME = "assuranceMember";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final AssuranceMemberService assuranceMemberService;
    private final AssuranceMemberRepository assuranceMemberRepository;


    @PostMapping("/assurance-members")
    public ResponseEntity<Object> createAssuranceMember(@Valid @RequestBody AssuranceMemberDTO assuranceMemberDTO) throws URISyntaxException {
        log.debug("REST request to save AssuranceMember : {}", assuranceMemberDTO);
        validateAssuranceMember(assuranceMemberDTO, true);

        AssuranceMemberDTO result = assuranceMemberService.save(assuranceMemberDTO);
        return createResponse(result, "/api/assurance-members/");
    }

    @PutMapping("/assurance-members/{id}")
    public ResponseEntity<Object> updateAssuranceMember(@PathVariable(value = "id", required = false) final Long id,
                                                        @Valid @RequestBody AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("REST request to update AssuranceMember : {}, {}", id, assuranceMemberDTO);
        validateAssuranceMember(assuranceMemberDTO, false);

        AssuranceMemberDTO result = assuranceMemberService.update(assuranceMemberDTO);
        return updateResponse(result, assuranceMemberDTO.getId());
    }

    @PatchMapping(value = "/assurance-members/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<AssuranceMemberDTO> partialUpdateAssuranceMember(@PathVariable(value = "id", required = false) final Long id,
                                                                           @NotNull @RequestBody AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("REST request to partial update AssuranceMember partially : {}, {}", id, assuranceMemberDTO);
        validatePatchAssuranceMember(id, assuranceMemberDTO);

        Optional<AssuranceMemberDTO> result = assuranceMemberService.partialUpdate(assuranceMemberDTO);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, assuranceMemberDTO.getId().toString()));
    }

    @GetMapping("/assurance-members")
    public List<AssuranceMemberDTO> getAllAssuranceMembers() {
        log.debug("REST request to get all AssuranceMembers");
        return assuranceMemberService.findAll();
    }

    @GetMapping("/assurance-members/{id}")
    public ResponseEntity<AssuranceMemberDTO> getAssuranceMember(@PathVariable Long id) {
        log.debug("REST request to get AssuranceMember : {}", id);
        Optional<AssuranceMemberDTO> assuranceMemberDTO = assuranceMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assuranceMemberDTO);
    }

    @DeleteMapping("/assurance-members/{id}")
    public ResponseEntity<Void> deleteAssuranceMember(@PathVariable Long id) {
        log.debug("REST request to delete AssuranceMember : {}", id);
        assuranceMemberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }

    private void validateAssuranceMember(AssuranceMemberDTO assuranceMemberDTO, boolean isNew) {
        if (isNew && assuranceMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new assuranceMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (assuranceMemberDTO.getStartDate() == null) {
            throw new BadRequestAlertException("A new assuranceMember must have start date", ENTITY_NAME, "startdaterequired");
        }
        if (assuranceMemberDTO.getEndDate() == null) {
            throw new BadRequestAlertException("A new assuranceMember must have end date", ENTITY_NAME, "enddaterequired");
        }
        if (assuranceMemberDTO.getAmountPayed() == null) {
            throw new BadRequestAlertException("A new assuranceMember must have amount payed", ENTITY_NAME, "amountpayedrequired");
        }
    }

    private void validatePatchAssuranceMember(Long id, AssuranceMemberDTO assuranceMemberDTO) {
        if (assuranceMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assuranceMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!assuranceMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    private ResponseEntity<Object> createResponse(AssuranceMemberDTO result, String path) throws URISyntaxException {
        if (result != null) {
            return ResponseEntity.created(new URI(path + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
        } else {
            String errorMessage = "The period of time you specified is already covered by an assurance for the selected user";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

    private ResponseEntity<Object> updateResponse(AssuranceMemberDTO result, Long id) {
        if (result != null) {
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString()))
                    .body(result);
        } else {
            String errorMessage = "The period of time you specified is already covered by an assurance for the selected user";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }
}

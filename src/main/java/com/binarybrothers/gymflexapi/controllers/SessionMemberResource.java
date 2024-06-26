package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.dtos.sessionmember.SessionMemberDTO;
import com.binarybrothers.gymflexapi.repositories.SessionMemberRepository;
import com.binarybrothers.gymflexapi.services.sessionmember.SessionMemberService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SessionMemberResource {

    private final Logger log = LoggerFactory.getLogger(SessionMemberResource.class);

    private static final String ENTITY_NAME = "sessionmember";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final SessionMemberService sessionMemberService;
    private final SessionMemberRepository sessionMemberRepository;


    @PostMapping("/session-members")
    public ResponseEntity<SessionMemberDTO> createSessionMember(@Valid @RequestBody SessionMemberDTO sessionMemberDTO) throws URISyntaxException {
        log.debug("REST request to save SessionMember : {}", sessionMemberDTO);
        if (sessionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionmember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (sessionMemberDTO.getEnteringTime() == null) {
            throw new BadRequestAlertException("A new sessionmember must have entering date", ENTITY_NAME, "enteringdaterequired");
        }
        SessionMemberDTO result = sessionMemberService.save(sessionMemberDTO);
        return ResponseEntity.created(new URI("/api/session-members/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/session-members/{id}")
    public ResponseEntity<SessionMemberDTO> updateSessionMember(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody SessionMemberDTO sessionMemberDTO) {
        log.debug("REST request to update SessionMember : {}, {}", id, sessionMemberDTO);
        if (sessionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionMemberDTO result = sessionMemberService.update(sessionMemberDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionMemberDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/session-members/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<SessionMemberDTO> partialUpdateSessionMember(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody SessionMemberDTO sessionMemberDTO) {
        log.debug("REST request to partial update SessionMember partially : {}, {}", id, sessionMemberDTO);
        if (sessionMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionMemberDTO> result = sessionMemberService.partialUpdate(sessionMemberDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionMemberDTO.getId().toString()));
    }

    @GetMapping("/session-members")
    public List<SessionMemberDTO> getAllSessionMembers() {
        log.debug("REST request to get all SessionMembers");
        return sessionMemberService.findAll();
    }

    @GetMapping("/web/session-members/member/{id}")
    public List<SessionMemberDTO> getAllSessionMembersByMember(@PathVariable Long id) {
        log.debug("REST request to get all SessionMembers");
        return sessionMemberService.findSessionsByMember(id);
    }

    @GetMapping("/session-members/{id}")
    public ResponseEntity<SessionMemberDTO> getSessionMember(@PathVariable Long id) {
        log.debug("REST request to get SessionMember : {}", id);
        Optional<SessionMemberDTO> sessionMemberDTO = sessionMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionMemberDTO);
    }

    @GetMapping("/session-members/entering/{qrCode}")
    public ResponseEntity<Integer> entering(@PathVariable String qrCode) {
        log.debug("REST request to get SessionMember : {}", qrCode);
        Integer canEnter = sessionMemberService.entering(qrCode);
        return ResponseEntity.ok().body(canEnter);
    }

    @DeleteMapping("/session-members/{id}")
    public ResponseEntity<Void> deleteSessionMember(@PathVariable Long id) {
        log.debug("REST request to delete SessionMember : {}", id);
        sessionMemberService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}

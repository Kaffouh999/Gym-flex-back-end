package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.repositories.SessionMemberRepository;
import com.example.GymInTheBack.services.sessionMember.SessionMemberService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SessionMemberResource {

    private final Logger log = LoggerFactory.getLogger(SessionMemberResource.class);

    private static final String ENTITY_NAME = "sessionMember";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final SessionMemberService sessionMemberService;


    private final SessionMemberRepository sessionMemberRepository;

    public SessionMemberResource(SessionMemberService sessionMemberService, SessionMemberRepository sessionMemberRepository) {
        this.sessionMemberService = sessionMemberService;
        this.sessionMemberRepository = sessionMemberRepository;
    }

    /**
     * {@code POST  /session-members} : Create a new sessionMember.
     *
     * @param sessionMemberDTO the sessionMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionMemberDTO, or with status {@code 400 (Bad Request)} if the sessionMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-members")
    public ResponseEntity<SessionMemberDTO> createSessionMember(@Valid @RequestBody SessionMemberDTO sessionMemberDTO) throws URISyntaxException {
        log.debug("REST request to save SessionMember : {}", sessionMemberDTO);
        if (sessionMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (sessionMemberDTO.getEnteringTime() == null) {
            throw new BadRequestAlertException("A new sessionMember must hve entreing date", ENTITY_NAME, "enteringdaterequired");
        }
        SessionMemberDTO result = sessionMemberService.save(sessionMemberDTO);
        return ResponseEntity.created(new URI("/api/session-members/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /session-members/:id} : Updates an existing sessionMember.
     *
     * @param id               the id of the sessionMemberDTO to save.
     * @param sessionMemberDTO the sessionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the sessionMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionMemberDTO couldn't be updated.
     */
    @PutMapping("/session-members/{id}")
    public ResponseEntity<SessionMemberDTO> updateSessionMember(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody SessionMemberDTO sessionMemberDTO) {
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, sessionMemberDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /session-members/:id} : Partial updates given fields of an existing sessionMember, field will ignore if it is null
     *
     * @param id               the id of the sessionMemberDTO to save.
     * @param sessionMemberDTO the sessionMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionMemberDTO,
     * or with status {@code 400 (Bad Request)} if the sessionMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionMemberDTO couldn't be updated.
     */
    @PatchMapping(value = "/session-members/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<SessionMemberDTO> partialUpdateSessionMember(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody SessionMemberDTO sessionMemberDTO) {
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

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, sessionMemberDTO.getId().toString()));
    }

    /**
     * {@code GET  /session-members} : get all the sessionMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionMembers in body.
     */
    @GetMapping("/session-members")
    public List<SessionMemberDTO> getAllSessionMembers() {
        log.debug("REST request to get all SessionMembers");
        return sessionMemberService.findAll();
    }

    @GetMapping("/web/session-members/member/{id}")
    public List<SessionMemberDTO> getAllSessionMembers(@PathVariable Long id) {
        log.debug("REST request to get all SessionMembers");
        return sessionMemberService.findSessionsByMember(id);
    }

    /**
     * {@code GET  /session-members/:id} : get the "id" sessionMember.
     *
     * @param id the id of the sessionMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionMemberDTO, or with status {@code 404 (Not Found)}.
     */
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

    /**
     * {@code DELETE  /session-members/:id} : delete the "id" sessionMember.
     *
     * @param id the id of the sessionMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-members/{id}")
    public ResponseEntity<Void> deleteSessionMember(@PathVariable Long id) {
        log.debug("REST request to delete SessionMember : {}", id);
        sessionMemberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}

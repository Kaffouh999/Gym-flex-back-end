package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.repositories.MemberRepository;
import com.example.GymInTheBack.services.member.MemberService;
import com.example.GymInTheBack.services.upload.IUploadService;
import com.example.GymInTheBack.services.user.OnlineUserService;
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
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    private static final String ENTITY_NAME = "member";


    private String applicationName = "GymFlex";

    private final MemberService memberService;

    private final OnlineUserService onlineUserService;
    private IUploadService uploadService;
    private final MemberRepository memberRepository;

    public MemberResource(MemberService memberService, MemberRepository memberRepository , OnlineUserService onlineUserService , IUploadService uploadService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.onlineUserService = onlineUserService;
        this.uploadService=uploadService;
    }

    /**
     * {@code POST  /members} : Create a new member.
     *
     * @param memberDTO the memberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberDTO, or with status {@code 400 (Bad Request)} if the member has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/members")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) throws URISyntaxException {
        log.debug("REST request to save Member : {}", memberDTO);
        OnlineUserDTO onlineUserDTO = memberDTO.getOnlineUser();
        if (memberDTO.getId() != null) {
            throw new BadRequestAlertException("A new member cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (memberDTO.getAge() == null) {
            throw new BadRequestAlertException("A new member must have age", ENTITY_NAME, "agerequired");
        }
        if (memberDTO.getCin() == null || memberDTO.getCin().trim().equals("") ) {
            throw new BadRequestAlertException("A new member must have cin", ENTITY_NAME, "cinrequired");
        }
        if (memberDTO.getGender() == null ) {
            throw new BadRequestAlertException("A new member must have gender", ENTITY_NAME, "genderrequired");
        }
        if(onlineUserDTO== null || onlineUserDTO.getId() == null ||onlineUserService.findById(onlineUserDTO.getId()).isEmpty()){
            onlineUserDTO = onlineUserService.save(onlineUserDTO);
            memberDTO.setOnlineUser(onlineUserDTO);
        }
        MemberDTO result = memberService.save(memberDTO);
        return ResponseEntity
            .created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /members/:id} : Updates an existing member.
     *
     * @param id the id of the memberDTO to save.
     * @param memberDTO the memberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberDTO,
     * or with status {@code 400 (Bad Request)} if the memberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/members/{id}")
    public ResponseEntity<MemberDTO> updateMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberDTO memberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Member : {}, {}", id, memberDTO);
        if (memberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MemberDTO result = memberService.update(memberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /members/:id} : Partial updates given fields of an existing member, field will ignore if it is null
     *
     * @param id the id of the memberDTO to save.
     * @param memberDTO the memberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberDTO,
     * or with status {@code 400 (Bad Request)} if the memberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemberDTO> partialUpdateMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberDTO memberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Member partially : {}, {}", id, memberDTO);
        if (memberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemberDTO> result = memberService.partialUpdate(memberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /members} : get all the members.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of members in body.
     */
    @GetMapping("/members")
    public List<MemberDTO> getAllMembers() {
        log.debug("REST request to get all Members");
        return memberService.findAll();
    }

    /**
     * {@code GET  /members/:id} : get the "id" member.
     *
     * @param id the id of the memberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        Optional<MemberDTO> memberDTO = memberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberDTO);
    }

    /**
     * {@code DELETE  /members/:id} : delete the "id" member.
     *
     * @param id the id of the memberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        Optional<Member> member = memberService.findById(id);

        if(member.get() != null) {
            String folderUrl = "/images/membersProfile/";
            String urlImage = member.get().getOnlineUser().getProfilePicture();
            uploadService.deleteDocument(folderUrl, urlImage);
        }
        memberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

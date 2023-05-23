package com.example.GymInTheBack.web;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.*;
import com.example.GymInTheBack.repositories.MemberRepository;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.services.mappers.MemberMapper;
import com.example.GymInTheBack.services.member.MemberService;
import com.example.GymInTheBack.services.upload.IUploadService;
import com.example.GymInTheBack.services.user.OnlineUserService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
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
    private final OnlineUserRepository onlineUserRepository;
    private final MemberMapper memberMapper;
    private IUploadService uploadService;
    private final MemberRepository memberRepository;

    public MemberResource(MemberService memberService, MemberRepository memberRepository , OnlineUserService onlineUserService , OnlineUserRepository onlineUserRepository, MemberMapper memberMapper, IUploadService uploadService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.onlineUserService = onlineUserService;
        this.onlineUserRepository = onlineUserRepository;
        this.memberMapper = memberMapper;
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
    public ResponseEntity<Object> createMember(@Valid @RequestBody MemberDTO memberDTO) throws URISyntaxException {
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
                try {
                MemberDTO result = memberService.saveMemberWithUser(memberDTO);
                    return ResponseEntity
                            .created(new URI("/api/members/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                }catch(DataIntegrityViolationException e){
                    String errorMessage = "Member with the same cin or email already exists.";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
                }



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

    @GetMapping("/members/user/{id}")
    public ResponseEntity<MemberDTO> getMemberByUser(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        Optional<OnlineUser> onlineUser = onlineUserRepository.findById(id);
        Member member = onlineUser.get().getMember();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(memberMapper.toDto(member)));
    }

    /**
     * {@code DELETE  /members/:id} : delete the "id" member.
     *
     * @param id the id of the memberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        Optional<Member> member = memberService.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if(!member.get().getAssuranceMemberList().isEmpty() || !member.get().getSubscriptionMemberList().isEmpty() ){
            String errorMessage = "Cannot delete member has associated : ";
            if(!member.get().getAssuranceMemberList().isEmpty()) {
                errorMessage += " \n assurances items : ";

                for (AssuranceMember assuranceMember : member.get().getAssuranceMemberList()) {
                    errorMessage += " (" + assuranceMember.getStartDate().format(formatter) + " " + assuranceMember.getAssurancAgency() + ") ";
                }
            }
            if(!member.get().getSubscriptionMemberList().isEmpty()) {
                errorMessage += " \n subscriptions : ";

                for (SubscriptionMember subscriptionMember : member.get().getSubscriptionMemberList()) {
                    errorMessage += " (" +subscriptionMember.getMember().getOnlineUser().getFirstName()+" "+ subscriptionMember.getStartDate().format(formatter) + " " + subscriptionMember.getPlan().getName() + ")";
                }
            }

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
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

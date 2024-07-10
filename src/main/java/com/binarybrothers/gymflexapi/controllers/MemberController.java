package com.binarybrothers.gymflexapi.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.statistics.MemberEquipmentStatistic;
import com.binarybrothers.gymflexapi.entities.*;
import com.binarybrothers.gymflexapi.repositories.MemberRepository;
import com.binarybrothers.gymflexapi.repositories.OnlineUserRepository;
import com.binarybrothers.gymflexapi.services.mappers.MemberMapper;
import com.binarybrothers.gymflexapi.services.mappers.OnlineUserMapper;
import com.binarybrothers.gymflexapi.services.member.MemberService;
import com.binarybrothers.gymflexapi.services.upload.IUploadService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(MemberController.class);
    private static final String ENTITY_NAME = "member";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final MemberService memberService;
    private final OnlineUserRepository onlineUserRepository;
    private final MemberMapper memberMapper;
    private final OnlineUserMapper onlineUserMapper;
    private final IUploadService uploadService;
    private final MemberRepository memberRepository;

    @PostMapping("/members")
    public ResponseEntity<Object> createMember(@Valid @RequestBody MemberDTO memberDTO) throws URISyntaxException {
        log.debug("REST request to save Member : {}", memberDTO);
        if (memberDTO.getId() != null) {
            throw new BadRequestAlertException("A new member cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (memberDTO.getAge() == null) {
            throw new BadRequestAlertException("A new member must have age", ENTITY_NAME, "agerequired");
        }
        if (memberDTO.getCin() == null || memberDTO.getCin().trim().isEmpty()) {
            throw new BadRequestAlertException("A new member must have cin", ENTITY_NAME, "cinrequired");
        }
        if (memberDTO.getGender() == null) {
            throw new BadRequestAlertException("A new member must have gender", ENTITY_NAME, "genderrequired");
        }
        try {
            MemberDTO result = memberService.saveMemberWithUser(memberDTO);
            return ResponseEntity.created(new URI("/api/members/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Member with the same cin or email already exists.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody MemberDTO memberDTO) {
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
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/members/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<MemberDTO> partialUpdateMember(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody MemberDTO memberDTO) {
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

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDTO.getId().toString()));
    }

    @GetMapping("/members")
    public List<MemberDTO> getAllMembers() {
        log.debug("REST request to get all Members");
        return memberService.findAll();
    }

    @GetMapping("/members/coach-members")
    public List<MemberDTO> getAllCoachMembers() {
        log.debug("REST request to get all Members");
        return memberService.findAllCoachMembers();
    }

    @GetMapping("/members/statistics")
    public MemberEquipmentStatistic getAllMembersEquipmentStatistic() {
        log.debug("REST request to get all Members");
        return memberRepository.getMemberEquipmentStatistics();
    }

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
        if (onlineUser.get().getMember() == null) {
            return ResponseUtil.wrapOrNotFound(Optional.of(new MemberDTO(onlineUserMapper.toDto(onlineUser.get()))));
        } else {
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(memberMapper.toDto(onlineUser.get().getMember())));
        }
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        Optional<Member> member = memberService.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (!member.get().getAssuranceMemberList().isEmpty() || !member.get().getSubscriptionMemberList().isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Cannot delete member has associated : ");
            if (!member.get().getAssuranceMemberList().isEmpty()) {
                errorMessage.append(" \n assurances items : ");

                for (AssuranceMember assuranceMember : member.get().getAssuranceMemberList()) {
                    errorMessage.append(" (").append(assuranceMember.getStartDate().format(formatter)).append(" ").append(assuranceMember.getAssurancAgency()).append(") ");
                }
            }
            if (!member.get().getSubscriptionMemberList().isEmpty()) {
                errorMessage.append(" \n subscriptions : ");

                for (SubscriptionMember subscriptionMember : member.get().getSubscriptionMemberList()) {
                    errorMessage.append(" (").append(subscriptionMember.getMember().getOnlineUser().getFirstName()).append(" ").append(subscriptionMember.getStartDate().format(formatter)).append(" ").append(subscriptionMember.getPlan().getName()).append(")");
                }
            }

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage.toString());
        }
        String folderUrl = "/images/membersProfile/";
        String urlImage = member.get().getOnlineUser().getProfilePicture();
        uploadService.deleteDocument(folderUrl, urlImage);
        memberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

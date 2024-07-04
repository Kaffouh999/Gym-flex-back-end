package com.binarybrothers.gymflexapi.services.member;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.repositories.MemberRepository;
import com.binarybrothers.gymflexapi.services.mappers.MemberMapper;
import com.binarybrothers.gymflexapi.services.user.OnlineUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;
    private final OnlineUserService onlineUserService;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper, OnlineUserService onlineUserService) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.onlineUserService = onlineUserService;
    }

    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        return memberMapper.toDto(member);
    }

    @Override
    public MemberDTO update(MemberDTO memberDTO) {
        log.debug("Request to update Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        return memberMapper.toDto(member);
    }

    @Override
    public Optional<MemberDTO> partialUpdate(MemberDTO memberDTO) {
        log.debug("Request to partially update Member : {}", memberDTO);

        return memberRepository
                .findById(memberDTO.getId())
                .map(existingMember -> {
                    memberMapper.partialUpdate(existingMember, memberDTO);

                    return existingMember;
                })
                .map(memberRepository::save)
                .map(memberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> findAll() {
        log.debug("Request to get all Members");
        return memberRepository.findAll().stream().map(memberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberDTO> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findById(id).map(memberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = DataIntegrityViolationException.class)
    public MemberDTO saveMemberWithUser(MemberDTO memberDTO) throws DataIntegrityViolationException {
        OnlineUserDTO onlineUserDTO = memberDTO.getOnlineUser();
        onlineUserDTO = onlineUserService.save(onlineUserDTO);
        memberDTO.setOnlineUser(onlineUserDTO);
        return save(memberDTO);


    }

    @Override
    public List<MemberDTO> findAllCoachMembers() {
        return memberRepository.findAllCoachMembers().stream().map(memberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}


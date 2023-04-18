package com.example.GymInTheBack.services.sessionMember;


import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.entities.SessionMember;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SessionMemberRepository;
import com.example.GymInTheBack.services.mappers.SessionMemberMapper;
import com.example.GymInTheBack.services.subscriptionMember.SubscriptionMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SessionMemberServiceImpl implements SessionMemberService {

    private final Logger log = LoggerFactory.getLogger(SessionMemberServiceImpl.class);

    private final SessionMemberRepository sessionMemberRepository;
    private final SubscriptionMemberService subscriptionMemberService;

    private final SessionMemberMapper sessionMemberMapper;

    public SessionMemberServiceImpl(SessionMemberRepository sessionMemberRepository, SessionMemberMapper sessionMemberMapper , SubscriptionMemberService subscriptionMemberService) {
        this.sessionMemberRepository = sessionMemberRepository;
        this.sessionMemberMapper = sessionMemberMapper;
        this.subscriptionMemberService = subscriptionMemberService;
    }

    @Override
    public SessionMemberDTO save(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to save SessionMember : {}", sessionMemberDTO);
        SessionMember sessionMember = sessionMemberMapper.toEntity(sessionMemberDTO);
        sessionMember = sessionMemberRepository.save(sessionMember);
        return sessionMemberMapper.toDto(sessionMember);
    }

    @Override
    public SessionMemberDTO update(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to update SessionMember : {}", sessionMemberDTO);
        SessionMember sessionMember = sessionMemberMapper.toEntity(sessionMemberDTO);
        sessionMember = sessionMemberRepository.save(sessionMember);
        return sessionMemberMapper.toDto(sessionMember);
    }

    @Override
    public Optional<SessionMemberDTO> partialUpdate(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to partially update SessionMember : {}", sessionMemberDTO);

        return sessionMemberRepository
            .findById(sessionMemberDTO.getId())
            .map(existingSessionMember -> {
                sessionMemberMapper.partialUpdate(existingSessionMember, sessionMemberDTO);

                return existingSessionMember;
            })
            .map(sessionMemberRepository::save)
            .map(sessionMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionMemberDTO> findAll() {
        log.debug("Request to get all SessionMembers");
        return sessionMemberRepository.findAll().stream().map(sessionMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionMemberDTO> findOne(Long id) {
        log.debug("Request to get SessionMember : {}", id);
        return sessionMemberRepository.findById(id).map(sessionMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionMember : {}", id);
        sessionMemberRepository.deleteById(id);
    }

    @Override
    public Integer entering(String qrCode) {
        List<SubscriptionMember> subscriptionMembersEntering = subscriptionMemberService.entering(qrCode);
        List<SessionMember> sessionMemberIn = sessionMemberRepository.alreadyIn(qrCode);
        SessionMember sessionMember ;
        if(subscriptionMembersEntering.size() > 0 ) {
            if (sessionMemberIn.size() == 0) {
                SubscriptionMember subscriptionMemberentering = subscriptionMembersEntering.get(0);
                sessionMember = new SessionMember();
                sessionMember.setSubscriptionMember(subscriptionMemberentering);
                ZonedDateTime currentDate = ZonedDateTime.now();
                sessionMember.setEnteringTime(currentDate);
                sessionMember.setGymBranch(subscriptionMemberentering.getMember().getGymBranch());
                sessionMember.setManagerAtTheTime(subscriptionMemberentering.getMember());
                sessionMemberRepository.save(sessionMember);
                return 1;
            } else  {
                sessionMember = sessionMemberIn.get(0);
                ZonedDateTime currentDate = ZonedDateTime.now();
                sessionMember.setLeavingTime(currentDate);
                sessionMemberRepository.save(sessionMember);
                return 2;
            }

        }
        return  0;
    }

    @Override
    public List<SessionMember> alreadyIn(String qrCode) {
        return sessionMemberRepository.alreadyIn(qrCode);
    }

}

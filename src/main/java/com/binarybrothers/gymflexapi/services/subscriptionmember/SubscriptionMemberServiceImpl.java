package com.binarybrothers.gymflexapi.services.subscriptionmember;


import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.binarybrothers.gymflexapi.repositories.SubscriptionMemberRepository;
import com.binarybrothers.gymflexapi.services.mappers.SubscriptionMemberMapper;
import com.binarybrothers.gymflexapi.services.mappers.SubscriptionMemberWithPaymentsMapper;
import com.binarybrothers.gymflexapi.utils.QRCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SubscriptionMemberServiceImpl implements SubscriptionMemberService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionMemberServiceImpl.class);

    private final SubscriptionMemberRepository subscriptionMemberRepository;


    private final SubscriptionMemberMapper subscriptionMemberMapper;
    private final SubscriptionMemberWithPaymentsMapper subscriptionMemberWithPaymentsMapper;

    public SubscriptionMemberServiceImpl(
            SubscriptionMemberRepository subscriptionMemberRepository,
            SubscriptionMemberMapper subscriptionMemberMapper,
            SubscriptionMemberWithPaymentsMapper subscriptionMemberWithPaymentsMapper) {
        this.subscriptionMemberRepository = subscriptionMemberRepository;
        this.subscriptionMemberMapper = subscriptionMemberMapper;
        this.subscriptionMemberWithPaymentsMapper = subscriptionMemberWithPaymentsMapper;
    }

    @Override
    public SubscriptionMemberDTO save(SubscriptionMemberDTO subscriptionMemberDTO) throws NoSuchAlgorithmException {
        log.debug("Request to save SubscriptionMember : {}", subscriptionMemberDTO);
        SubscriptionMember subscriptionMember = subscriptionMemberMapper.toEntity(subscriptionMemberDTO);
        String qrCode = QRCodeGenerator.generateUniqueCode();
        subscriptionMember.setCodeSubscription(qrCode);
        subscriptionMember = subscriptionMemberRepository.save(subscriptionMember);
        return subscriptionMemberMapper.toDto(subscriptionMember);
    }

    @Override
    public SubscriptionMemberDTO update(SubscriptionMemberDTO subscriptionMemberDTO) {
        log.debug("Request to update SubscriptionMember : {}", subscriptionMemberDTO);
        SubscriptionMember subscriptionMember = subscriptionMemberMapper.toEntity(subscriptionMemberDTO);
        subscriptionMember = subscriptionMemberRepository.save(subscriptionMember);
        return subscriptionMemberMapper.toDto(subscriptionMember);
    }

    @Override
    public Optional<SubscriptionMemberDTO> partialUpdate(SubscriptionMemberDTO subscriptionMemberDTO) {
        log.debug("Request to partially update SubscriptionMember : {}", subscriptionMemberDTO);

        return subscriptionMemberRepository
                .findById(subscriptionMemberDTO.getId())
                .map(existingSubscriptionMember -> {
                    subscriptionMemberMapper.partialUpdate(existingSubscriptionMember, subscriptionMemberDTO);

                    return existingSubscriptionMember;
                })
                .map(subscriptionMemberRepository::save)
                .map(subscriptionMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionMemberDTO> findAll() {
        log.debug("Request to get all SubscriptionMembers");
        Sort sort = Sort.by("id").ascending();
        return subscriptionMemberRepository
                .findAll(sort)
                .stream()
                .map(subscriptionMemberMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionMemberDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionMember : {}", id);
        return subscriptionMemberRepository.findById(id).map(subscriptionMemberMapper::toDto);
    }

    @Override
    public Optional<SubscriptionMemberDTO> findByCodeSubscription(String codeSubscription) {
        return subscriptionMemberRepository.findByCodeSubscription(codeSubscription).map(subscriptionMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionMember : {}", id);
        subscriptionMemberRepository.deleteById(id);
    }

    @Override
    public List<SubscriptionMember> entering(String qrCode) {
        return subscriptionMemberRepository.entering(qrCode);

    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionWithPaymentsDTO> findByMemberId(Long userId) {
        return subscriptionMemberRepository.findByMemberId(userId).stream().map(subscriptionMemberWithPaymentsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<SubscriptionMemberDTO> searchSubs(SubscriptionMemberDTO subscriptionMemberDTO) {
        SubscriptionMember subscriptionMember = subscriptionMemberMapper.toEntity(subscriptionMemberDTO);
        Long membeId = subscriptionMember.getMember() != null ? subscriptionMember.getMember().getId():null;
         Long planId = subscriptionMember.getPlan() != null? subscriptionMember.getPlan().getId():null;
         Timestamp startDate = subscriptionMember.getStartDate() != null? Timestamp.from(subscriptionMember.getStartDate().toInstant()):null;
        Timestamp endDate = subscriptionMember.getEndDate() != null? Timestamp.from(subscriptionMember.getEndDate().toInstant()):null;

        return subscriptionMemberRepository.searchSubscriptions( membeId ,  planId , startDate,endDate).stream().map(subscriptionMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }


}

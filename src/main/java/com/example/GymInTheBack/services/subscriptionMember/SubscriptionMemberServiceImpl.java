package com.example.GymInTheBack.services.subscriptionMember;


import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SubscriptionMemberRepository;
import com.example.GymInTheBack.services.mappers.SubscriptionMemberMapper;
import com.example.GymInTheBack.services.mappers.SubscriptionMemberWithPaymentsMapper;
import com.example.GymInTheBack.utils.QRCodeGenerator;
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
        System.out.println(qrCode.length());
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

        System.out.println(startDate);
        System.out.println(endDate);

        return subscriptionMemberRepository.searchSubscriptions( membeId ,  planId , startDate,endDate).stream().map(subscriptionMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }


}

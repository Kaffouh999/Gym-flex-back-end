package com.example.GymInTheBack.services.subscriptionMember;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.SubscriptionMemberDTO;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SubscriptionMemberRepository;
import com.example.GymInTheBack.services.mappers.SubscriptionMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SubscriptionMemberServiceImpl implements SubscriptionMemberService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionMemberServiceImpl.class);

    private final SubscriptionMemberRepository subscriptionMemberRepository;

    private final SubscriptionMemberMapper subscriptionMemberMapper;

    public SubscriptionMemberServiceImpl(
        SubscriptionMemberRepository subscriptionMemberRepository,
        SubscriptionMemberMapper subscriptionMemberMapper
    ) {
        this.subscriptionMemberRepository = subscriptionMemberRepository;
        this.subscriptionMemberMapper = subscriptionMemberMapper;
    }

    @Override
    public SubscriptionMemberDTO save(SubscriptionMemberDTO subscriptionMemberDTO) {
        log.debug("Request to save SubscriptionMember : {}", subscriptionMemberDTO);
        SubscriptionMember subscriptionMember = subscriptionMemberMapper.toEntity(subscriptionMemberDTO);
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
        return subscriptionMemberRepository
            .findAll()
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
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionMember : {}", id);
        subscriptionMemberRepository.deleteById(id);
    }
}
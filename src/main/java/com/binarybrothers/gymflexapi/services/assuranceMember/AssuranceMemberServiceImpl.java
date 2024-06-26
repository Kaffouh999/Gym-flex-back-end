package com.binarybrothers.gymflexapi.services.assuranceMember;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.binarybrothers.gymflexapi.dtos.assurancemember.AssuranceMemberDTO;
import com.binarybrothers.gymflexapi.entities.AssuranceMember;
import com.binarybrothers.gymflexapi.repositories.AssuranceMemberRepository;
import com.binarybrothers.gymflexapi.services.mappers.AssuranceMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssuranceMemberServiceImpl implements AssuranceMemberService {

    private final Logger log = LoggerFactory.getLogger(AssuranceMemberServiceImpl.class);

    private final AssuranceMemberRepository assuranceMemberRepository;

    private final AssuranceMemberMapper assuranceMemberMapper;

    public AssuranceMemberServiceImpl(AssuranceMemberRepository assuranceMemberRepository, AssuranceMemberMapper assuranceMemberMapper) {
        this.assuranceMemberRepository = assuranceMemberRepository;
        this.assuranceMemberMapper = assuranceMemberMapper;
    }

    @Override
    public AssuranceMemberDTO save(AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("Request to save AssuranceMember : {}", assuranceMemberDTO);
        List<AssuranceMember> assurancesINterferWithDate =  assuranceMemberRepository.findIntersectingAssurances(assuranceMemberDTO.getStartDate(),assuranceMemberDTO.getEndDate(),assuranceMemberDTO.getMember().getId());
       if(assurancesINterferWithDate.isEmpty()) {
           AssuranceMember assuranceMember = assuranceMemberMapper.toEntity(assuranceMemberDTO);
           assuranceMember = assuranceMemberRepository.save(assuranceMember);
           return assuranceMemberMapper.toDto(assuranceMember);
       }else{
           return  null;
       }
    }

    @Override
    public AssuranceMemberDTO update(AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("Request to update AssuranceMember : {}", assuranceMemberDTO);
        List<AssuranceMember> assurancesINterferWithDate =  assuranceMemberRepository.findIntersectingAssurances(assuranceMemberDTO.getStartDate(),assuranceMemberDTO.getEndDate(),assuranceMemberDTO.getMember().getId());
        if(assurancesINterferWithDate.isEmpty()) {
        AssuranceMember assuranceMember = assuranceMemberMapper.toEntity(assuranceMemberDTO);
        assuranceMember = assuranceMemberRepository.save(assuranceMember);
        return assuranceMemberMapper.toDto(assuranceMember);
        }else{
            return  null;
        }
    }

    @Override
    public Optional<AssuranceMemberDTO> partialUpdate(AssuranceMemberDTO assuranceMemberDTO) {
        log.debug("Request to partially update AssuranceMember : {}", assuranceMemberDTO);

        return assuranceMemberRepository
            .findById(assuranceMemberDTO.getId())
            .map(existingAssuranceMember -> {
                assuranceMemberMapper.partialUpdate(existingAssuranceMember, assuranceMemberDTO);

                return existingAssuranceMember;
            })
            .map(assuranceMemberRepository::save)
            .map(assuranceMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssuranceMemberDTO> findAll() {
        log.debug("Request to get all AssuranceMembers");
        return assuranceMemberRepository
            .findAll()
            .stream()
            .map(assuranceMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssuranceMemberDTO> findOne(Long id) {
        log.debug("Request to get AssuranceMember : {}", id);
        return assuranceMemberRepository.findById(id).map(assuranceMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssuranceMember : {}", id);
        assuranceMemberRepository.deleteById(id);
    }
}

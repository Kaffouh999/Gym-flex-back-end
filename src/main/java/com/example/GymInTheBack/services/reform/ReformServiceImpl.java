package com.example.GymInTheBack.services.reform;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.reform.ReformDTO;
import com.example.GymInTheBack.entities.Maintining;
import com.example.GymInTheBack.entities.Reform;
import com.example.GymInTheBack.repositories.ReformRepository;
import com.example.GymInTheBack.services.mappers.ReformMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ReformServiceImpl implements ReformService {

    private final Logger log = LoggerFactory.getLogger(ReformServiceImpl.class);

    private final ReformRepository reformRepository;

    private final ReformMapper reformMapper;

    public ReformServiceImpl(ReformRepository reformRepository, ReformMapper reformMapper) {
        this.reformRepository = reformRepository;
        this.reformMapper = reformMapper;
    }

    @Override
    public ReformDTO save(ReformDTO reformDTO) {
        log.debug("Request to save Reform : {}", reformDTO);
        List<Reform> oldREform =  reformRepository.findOldReform(reformDTO.getItem().getId());
        if(oldREform.isEmpty()) {
            Reform reform = reformMapper.toEntity(reformDTO);
            reform = reformRepository.save(reform);
            return reformMapper.toDto(reform);
        }else{
            return null;
        }
    }

    @Override
    public ReformDTO update(ReformDTO reformDTO) {
        log.debug("Request to update Reform : {}", reformDTO);
        List<Reform> oldREform =  reformRepository.findOldReform(reformDTO.getItem().getId());
        if(oldREform.isEmpty()) {
            Reform reform = reformMapper.toEntity(reformDTO);
            reform = reformRepository.save(reform);
            return reformMapper.toDto(reform);
        }else{
            return  null;
        }
    }

    @Override
    public Optional<ReformDTO> partialUpdate(ReformDTO reformDTO) {
        log.debug("Request to partially update Reform : {}", reformDTO);

        return reformRepository
            .findById(reformDTO.getId())
            .map(existingReform -> {
                reformMapper.partialUpdate(existingReform, reformDTO);

                return existingReform;
            })
            .map(reformRepository::save)
            .map(reformMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReformDTO> findAll() {
        log.debug("Request to get all Reforms");
        return reformRepository.findAll().stream().map(reformMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReformDTO> findOne(Long id) {
        log.debug("Request to get Reform : {}", id);
        return reformRepository.findById(id).map(reformMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reform : {}", id);
        reformRepository.deleteById(id);
    }
}

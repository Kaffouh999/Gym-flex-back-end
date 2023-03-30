package com.example.GymInTheBack.services.gymbranch;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.repositories.GymBranchRepository;
import com.example.GymInTheBack.services.mappers.GymBranchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GymBranchServiceImpl implements GymBranchService {

    private final Logger log = LoggerFactory.getLogger(GymBranchServiceImpl.class);

    private final GymBranchRepository gymBranchRepository;

    private final GymBranchMapper gymBranchMapper;

    public GymBranchServiceImpl(GymBranchRepository gymBranchRepository, GymBranchMapper gymBranchMapper) {
        this.gymBranchRepository = gymBranchRepository;
        this.gymBranchMapper = gymBranchMapper;
    }

    @Override
    public GymBranchDTO save(GymBranchDTO gymBranchDTO) {
        log.debug("Request to save GymBranch : {}", gymBranchDTO);
        GymBranch gymBranch = gymBranchMapper.toEntity(gymBranchDTO);
        gymBranch = gymBranchRepository.save(gymBranch);
        return gymBranchMapper.toDto(gymBranch);
    }

    @Override
    public GymBranchDTO update(GymBranchDTO gymBranchDTO) {
        log.debug("Request to update GymBranch : {}", gymBranchDTO);
        GymBranch gymBranch = gymBranchMapper.toEntity(gymBranchDTO);
        gymBranch = gymBranchRepository.save(gymBranch);
        return gymBranchMapper.toDto(gymBranch);
    }

    @Override
    public Optional<GymBranchDTO> partialUpdate(GymBranchDTO gymBranchDTO) {
        log.debug("Request to partially update GymBranch : {}", gymBranchDTO);

        return gymBranchRepository
            .findById(gymBranchDTO.getId())
            .map(existingGymBranch -> {
                gymBranchMapper.partialUpdate(existingGymBranch, gymBranchDTO);

                return existingGymBranch;
            })
            .map(gymBranchRepository::save)
            .map(gymBranchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymBranchDTO> findAll() {
        log.debug("Request to get all GymBranches");
        return gymBranchRepository.findAll().stream().map(gymBranchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GymBranchDTO> findOne(Long id) {
        log.debug("Request to get GymBranch : {}", id);
        return gymBranchRepository.findById(id).map(gymBranchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GymBranch : {}", id);
        gymBranchRepository.deleteById(id);
    }
}

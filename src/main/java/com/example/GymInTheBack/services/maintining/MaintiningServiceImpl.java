package com.example.GymInTheBack.services.maintining;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.maintining.MaintiningDTO;
import com.example.GymInTheBack.entities.AssuranceMember;
import com.example.GymInTheBack.entities.Maintining;
import com.example.GymInTheBack.repositories.MaintiningRepository;
import com.example.GymInTheBack.services.mappers.MaintiningMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MaintiningServiceImpl implements MaintiningService {

    private final Logger log = LoggerFactory.getLogger(MaintiningServiceImpl.class);

    private final MaintiningRepository maintiningRepository;

    private final MaintiningMapper maintiningMapper;

    public MaintiningServiceImpl(MaintiningRepository maintiningRepository, MaintiningMapper maintiningMapper) {
        this.maintiningRepository = maintiningRepository;
        this.maintiningMapper = maintiningMapper;
    }

    @Override
    public MaintiningDTO save(MaintiningDTO maintiningDTO) {
        log.debug("Request to save Maintining : {}", maintiningDTO);
        List<Maintining> maintiningINterferWithDate =  maintiningRepository.findIntersectingMaintinings(maintiningDTO.getStartDate(),maintiningDTO.getEndDate(),maintiningDTO.getItem().getId());
        if(maintiningINterferWithDate.isEmpty()) {
            Maintining maintining = maintiningMapper.toEntity(maintiningDTO);
            maintining = maintiningRepository.save(maintining);
            return maintiningMapper.toDto(maintining);
        }else{
            return null;
        }
    }

    @Override
    public MaintiningDTO update(MaintiningDTO maintiningDTO) {
        log.debug("Request to update Maintining : {}", maintiningDTO);
        List<Maintining> maintiningINterferWithDate =  maintiningRepository.findIntersectingMaintinings(maintiningDTO.getStartDate(),maintiningDTO.getEndDate(),maintiningDTO.getItem().getId());
        if(maintiningINterferWithDate.isEmpty()) {
            Maintining maintining = maintiningMapper.toEntity(maintiningDTO);
            maintining = maintiningRepository.save(maintining);
            return maintiningMapper.toDto(maintining);
        }else{
            return null;
        }
    }

    @Override
    public Optional<MaintiningDTO> partialUpdate(MaintiningDTO maintiningDTO) {
        log.debug("Request to partially update Maintining : {}", maintiningDTO);

        return maintiningRepository
            .findById(maintiningDTO.getId())
            .map(existingMaintining -> {
                maintiningMapper.partialUpdate(existingMaintining, maintiningDTO);

                return existingMaintining;
            })
            .map(maintiningRepository::save)
            .map(maintiningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintiningDTO> findAll() {
        log.debug("Request to get all Maintinings");
        return maintiningRepository.findAll().stream().map(maintiningMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintiningDTO> findOne(Long id) {
        log.debug("Request to get Maintining : {}", id);
        return maintiningRepository.findById(id).map(maintiningMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Maintining : {}", id);
        maintiningRepository.deleteById(id);
    }
}

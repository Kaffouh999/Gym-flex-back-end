package com.example.GymInTheBack.services.equipmentItem;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.equipmentItem.EquipmentItemDTO;
import com.example.GymInTheBack.entities.EquipmentItem;
import com.example.GymInTheBack.repositories.EquipmentItemRepository;
import com.example.GymInTheBack.services.mappers.EquipmentItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EquipmentItemServiceImpl implements EquipmentItemService {

    private final Logger log = LoggerFactory.getLogger(EquipmentItemServiceImpl.class);

    private final EquipmentItemRepository equipmentItemRepository;

    private final EquipmentItemMapper equipmentItemMapper;

    public EquipmentItemServiceImpl(EquipmentItemRepository equipmentItemRepository, EquipmentItemMapper equipmentItemMapper) {
        this.equipmentItemRepository = equipmentItemRepository;
        this.equipmentItemMapper = equipmentItemMapper;
    }

    @Override
    public EquipmentItemDTO save(EquipmentItemDTO equipmentItemDTO) {
        log.debug("Request to save EquipmentItem : {}", equipmentItemDTO);
        EquipmentItem equipmentItem = equipmentItemMapper.toEntity(equipmentItemDTO);
        equipmentItem = equipmentItemRepository.save(equipmentItem);
        return equipmentItemMapper.toDto(equipmentItem);
    }

    @Override
    public EquipmentItemDTO update(EquipmentItemDTO equipmentItemDTO) {
        log.debug("Request to update EquipmentItem : {}", equipmentItemDTO);
        EquipmentItem equipmentItem = equipmentItemMapper.toEntity(equipmentItemDTO);
        equipmentItem = equipmentItemRepository.save(equipmentItem);
        return equipmentItemMapper.toDto(equipmentItem);
    }

    @Override
    public Optional<EquipmentItemDTO> partialUpdate(EquipmentItemDTO equipmentItemDTO) {
        log.debug("Request to partially update EquipmentItem : {}", equipmentItemDTO);

        return equipmentItemRepository
            .findById(equipmentItemDTO.getId())
            .map(existingEquipmentItem -> {
                equipmentItemMapper.partialUpdate(existingEquipmentItem, equipmentItemDTO);

                return existingEquipmentItem;
            })
            .map(equipmentItemRepository::save)
            .map(equipmentItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentItemDTO> findAll() {
        log.debug("Request to get all EquipmentItems");
        return equipmentItemRepository.findAll().stream().map(equipmentItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipmentItemDTO> findOne(Long id) {
        log.debug("Request to get EquipmentItem : {}", id);
        return equipmentItemRepository.findById(id).map(equipmentItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EquipmentItem : {}", id);
        equipmentItemRepository.deleteById(id);
    }
}

package com.binarybrothers.gymflexapi.services.equipment;


import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.binarybrothers.gymflexapi.dtos.equipment.EquipmentDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.repositories.EquipmentRepository;
import com.binarybrothers.gymflexapi.services.mappers.EquipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    private final EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EquipmentMapper equipmentMapper) {
        this.equipmentRepository = equipmentRepository;
        this.equipmentMapper = equipmentMapper;
    }

    @Override
    public EquipmentDTO save(EquipmentDTO equipmentDTO) {
        log.debug("Request to save Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    @Override
    public EquipmentDTO update(EquipmentDTO equipmentDTO) {
        log.debug("Request to update Equipment : {}", equipmentDTO);
        Equipment equipment = equipmentMapper.toEntity(equipmentDTO);
        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(equipment);
    }

    @Override
    public Optional<EquipmentDTO> partialUpdate(EquipmentDTO equipmentDTO) {
        log.debug("Request to partially update Equipment : {}", equipmentDTO);

        return equipmentRepository
            .findById(equipmentDTO.getId())
            .map(existingEquipment -> {
                equipmentMapper.partialUpdate(existingEquipment, equipmentDTO);

                return existingEquipment;
            })
            .map(equipmentRepository::save)
            .map(equipmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentDTO> findAll() {
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll().stream().map(equipmentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipmentDTO> findOne(Long id) {
        log.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id).map(equipmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equipment : {}", id);
        equipmentRepository.deleteById(id);
    }

    @Override
    public void deleteImage(Long id) {
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        
        if(equipment.get() != null) {

            String urlImage = equipment.get().getImageUrl();


            int lastIndex = urlImage.lastIndexOf("/");
            String imageName = urlImage.substring(lastIndex + 1);
            // Path to the image file
            String folderPath = "C:\\youssef.com\\";

            String imagePath = folderPath + imageName ;
            // Create a File object representing the image file
            File imageFile = new File(imagePath);

            // Delete the image file
            if (imageFile.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        }
    }

    @Override
    public Optional<Equipment> findById(Long id) {
        return equipmentRepository.findById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return equipmentRepository.findByName(name) != null ? true:false;
    }
}

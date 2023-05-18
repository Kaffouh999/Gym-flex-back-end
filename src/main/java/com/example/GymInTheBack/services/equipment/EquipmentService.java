package com.example.GymInTheBack.services.equipment;

import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.entities.Equipment;

import java.util.List;
import java.util.Optional;


public interface EquipmentService {
    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save.
     * @return the persisted entity.
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     * Updates a equipment.
     *
     * @param equipmentDTO the entity to update.
     * @return the persisted entity.
     */
    EquipmentDTO update(EquipmentDTO equipmentDTO);

    /**
     * Partially updates a equipment.
     *
     * @param equipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipmentDTO> partialUpdate(EquipmentDTO equipmentDTO);

    /**
     * Get all the equipment.
     *
     * @return the list of entities.
     */
    List<EquipmentDTO> findAll();

    /**
     * Get the "id" equipment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipmentDTO> findOne(Long id);

    /**
     * Delete the "id" equipment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteImage(Long id);

    Optional<Equipment> findById(Long id);
    boolean existsByName(String name);
}

package com.binarybrothers.gymflexapi.services.equipmentItem;

import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public interface EquipmentItemService {
    /**
     * Save a equipmentItem.
     *
     * @param equipmentItemDTO the entity to save.
     * @return the persisted entity.
     */
    EquipmentItemDTO save(EquipmentItemDTO equipmentItemDTO) throws NoSuchAlgorithmException;

    /**
     * Updates a equipmentItem.
     *
     * @param equipmentItemDTO the entity to update.
     * @return the persisted entity.
     */
    EquipmentItemDTO update(EquipmentItemDTO equipmentItemDTO);

    /**
     * Partially updates a equipmentItem.
     *
     * @param equipmentItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipmentItemDTO> partialUpdate(EquipmentItemDTO equipmentItemDTO);

    /**
     * Get all the equipmentItems.
     *
     * @return the list of entities.
     */
    List<EquipmentItemDTO> findAll();

    /**
     * Get the "id" equipmentItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipmentItemDTO> findOne(Long id);

    /**
     * Delete the "id" equipmentItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<EquipmentItemDTO> findByBareCode(String bareCode);
}

package com.binarybrothers.gymflexapi.services.maintining;


import com.binarybrothers.gymflexapi.dtos.maintining.MaintiningDTO;

import java.util.List;
import java.util.Optional;


public interface MaintiningService {
    /**
     * Save a maintining.
     *
     * @param maintiningDTO the entity to save.
     * @return the persisted entity.
     */
    MaintiningDTO save(MaintiningDTO maintiningDTO);

    /**
     * Updates a maintining.
     *
     * @param maintiningDTO the entity to update.
     * @return the persisted entity.
     */
    MaintiningDTO update(MaintiningDTO maintiningDTO);

    /**
     * Partially updates a maintining.
     *
     * @param maintiningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintiningDTO> partialUpdate(MaintiningDTO maintiningDTO);

    /**
     * Get all the maintinings.
     *
     * @return the list of entities.
     */
    List<MaintiningDTO> findAll();

    /**
     * Get the "id" maintining.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintiningDTO> findOne(Long id);

    /**
     * Delete the "id" maintining.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

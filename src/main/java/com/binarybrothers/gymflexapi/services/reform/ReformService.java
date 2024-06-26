package com.binarybrothers.gymflexapi.services.reform;


import com.binarybrothers.gymflexapi.dtos.reform.ReformDTO;

import java.util.List;
import java.util.Optional;


public interface ReformService {
    /**
     * Save a reform.
     *
     * @param reformDTO the entity to save.
     * @return the persisted entity.
     */
    ReformDTO save(ReformDTO reformDTO);

    /**
     * Updates a reform.
     *
     * @param reformDTO the entity to update.
     * @return the persisted entity.
     */
    ReformDTO update(ReformDTO reformDTO);

    /**
     * Partially updates a reform.
     *
     * @param reformDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReformDTO> partialUpdate(ReformDTO reformDTO);

    /**
     * Get all the reforms.
     *
     * @return the list of entities.
     */
    List<ReformDTO> findAll();

    /**
     * Get the "id" reform.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReformDTO> findOne(Long id);

    /**
     * Delete the "id" reform.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

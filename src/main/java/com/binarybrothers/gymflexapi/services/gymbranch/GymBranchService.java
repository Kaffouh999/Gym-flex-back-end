package com.binarybrothers.gymflexapi.services.gymbranch;


import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;

import java.util.List;
import java.util.Optional;


public interface GymBranchService {
    /**
     * Save a gymBranch.
     *
     * @param gymBranchDTO the entity to save.
     * @return the persisted entity.
     */
    GymBranchDTO save(GymBranchDTO gymBranchDTO);

    /**
     * Updates a gymBranch.
     *
     * @param gymBranchDTO the entity to update.
     * @return the persisted entity.
     */
    GymBranchDTO update(GymBranchDTO gymBranchDTO);

    /**
     * Partially updates a gymBranch.
     *
     * @param gymBranchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GymBranchDTO> partialUpdate(GymBranchDTO gymBranchDTO);

    /**
     * Get all the gymBranches.
     *
     * @return the list of entities.
     */
    List<GymBranchDTO> findAll();

    /**
     * Get the "id" gymBranch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GymBranchDTO> findOne(Long id);

    /**
     * Delete the "id" gymBranch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean existsByName(String name);
}

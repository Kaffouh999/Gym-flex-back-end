package com.binarybrothers.gymflexapi.services.assuranceMember;

import com.binarybrothers.gymflexapi.dtos.assurancemember.AssuranceMemberDTO;

import java.util.List;
import java.util.Optional;


public interface AssuranceMemberService {
    /**
     * Save a assurancemember.
     *
     * @param assuranceMemberDTO the entity to save.
     * @return the persisted entity.
     */
    AssuranceMemberDTO save(AssuranceMemberDTO assuranceMemberDTO);

    /**
     * Updates a assurancemember.
     *
     * @param assuranceMemberDTO the entity to update.
     * @return the persisted entity.
     */
    AssuranceMemberDTO update(AssuranceMemberDTO assuranceMemberDTO);

    /**
     * Partially updates a assurancemember.
     *
     * @param assuranceMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssuranceMemberDTO> partialUpdate(AssuranceMemberDTO assuranceMemberDTO);

    /**
     * Get all the assuranceMembers.
     *
     * @return the list of entities.
     */
    List<AssuranceMemberDTO> findAll();

    /**
     * Get the "id" assurancemember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssuranceMemberDTO> findOne(Long id);

    /**
     * Delete the "id" assurancemember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

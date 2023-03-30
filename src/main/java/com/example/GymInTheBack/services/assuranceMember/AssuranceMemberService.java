package com.example.GymInTheBack.services.assuranceMember;

import com.example.GymInTheBack.dtos.assuranceMember.AssuranceMemberDTO;

import java.util.List;
import java.util.Optional;


public interface AssuranceMemberService {
    /**
     * Save a assuranceMember.
     *
     * @param assuranceMemberDTO the entity to save.
     * @return the persisted entity.
     */
    AssuranceMemberDTO save(AssuranceMemberDTO assuranceMemberDTO);

    /**
     * Updates a assuranceMember.
     *
     * @param assuranceMemberDTO the entity to update.
     * @return the persisted entity.
     */
    AssuranceMemberDTO update(AssuranceMemberDTO assuranceMemberDTO);

    /**
     * Partially updates a assuranceMember.
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
     * Get the "id" assuranceMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssuranceMemberDTO> findOne(Long id);

    /**
     * Delete the "id" assuranceMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

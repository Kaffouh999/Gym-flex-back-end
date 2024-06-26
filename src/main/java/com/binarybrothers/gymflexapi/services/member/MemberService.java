package com.binarybrothers.gymflexapi.services.member;


import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.entities.Member;

import java.util.List;
import java.util.Optional;


public interface MemberService {
    /**
     * Save a member.
     *
     * @param memberDTO the entity to save.
     * @return the persisted entity.
     */
    MemberDTO save(MemberDTO memberDTO);

    /**
     * Updates a member.
     *
     * @param memberDTO the entity to update.
     * @return the persisted entity.
     */
    MemberDTO update(MemberDTO memberDTO);

    /**
     * Partially updates a member.
     *
     * @param memberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MemberDTO> partialUpdate(MemberDTO memberDTO);

    /**
     * Get all the members.
     *
     * @return the list of entities.
     */
    List<MemberDTO> findAll();

    /**
     * Get the "id" member.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MemberDTO> findOne(Long id);

    /**
     * Delete the "id" member.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    public Optional<Member> findById(Long id);

    MemberDTO saveMemberWithUser(MemberDTO member);
}

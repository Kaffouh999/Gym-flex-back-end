package com.example.GymInTheBack.services.sessionMember;


import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.dtos.statistics.EnteringTimeStatisticDTO;
import com.example.GymInTheBack.entities.SessionMember;

import java.util.List;
import java.util.Optional;


public interface SessionMemberService {
    /**
     * Save a sessionMember.
     *
     * @param sessionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    SessionMemberDTO save(SessionMemberDTO sessionMemberDTO);

    /**
     * Updates a sessionMember.
     *
     * @param sessionMemberDTO the entity to update.
     * @return the persisted entity.
     */
    SessionMemberDTO update(SessionMemberDTO sessionMemberDTO);

    /**
     * Partially updates a sessionMember.
     *
     * @param sessionMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionMemberDTO> partialUpdate(SessionMemberDTO sessionMemberDTO);

    /**
     * Get all the sessionMembers.
     *
     * @return the list of entities.
     */
    List<SessionMemberDTO> findAll();

    /**
     * Get the "id" sessionMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionMemberDTO> findOne(Long id);

    /**
     * Delete the "id" sessionMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    Integer entering(String qrCode);

    List<SessionMember> alreadyIn(String qrCode);

    List<SessionMemberDTO> findSessionsByMember(Long idMember);

    List<EnteringTimeStatisticDTO> getSessionStatistic(Long idGymBranch);
}

package com.binarybrothers.gymflexapi.services.sessionmember;


import com.binarybrothers.gymflexapi.dtos.sessionmember.SessionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.statistics.EnteringTimeStatisticDTO;
import com.binarybrothers.gymflexapi.entities.SessionMember;

import java.util.List;
import java.util.Optional;


public interface SessionMemberService {
    /**
     * Save a sessionmember.
     *
     * @param sessionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    SessionMemberDTO save(SessionMemberDTO sessionMemberDTO);

    /**
     * Updates a sessionmember.
     *
     * @param sessionMemberDTO the entity to update.
     * @return the persisted entity.
     */
    SessionMemberDTO update(SessionMemberDTO sessionMemberDTO);

    /**
     * Partially updates a sessionmember.
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
     * Get the "id" sessionmember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionMemberDTO> findOne(Long id);

    /**
     * Delete the "id" sessionmember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    Integer entering(String qrCode);

    List<SessionMember> alreadyIn(String qrCode);

    List<SessionMemberDTO> findSessionsByMember(Long idMember);

    List<EnteringTimeStatisticDTO> getSessionStatistic(Long idGymBranch);
}

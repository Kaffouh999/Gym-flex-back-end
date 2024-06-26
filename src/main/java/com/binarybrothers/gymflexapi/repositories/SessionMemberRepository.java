package com.binarybrothers.gymflexapi.repositories;


import com.binarybrothers.gymflexapi.entities.SessionMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the SessionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionMemberRepository extends JpaRepository<SessionMember, Long> {

    @Query("select e from SessionMember e WHERE subscriptionMember.codeSubscription = :qrCode and  DAY(e.enteringTime) = DAY(CURRENT_DATE) AND MONTH(e.enteringTime) = MONTH(CURRENT_DATE) AND YEAR(e.enteringTime) = YEAR(CURRENT_DATE)")
    List<SessionMember> alreadyIn(String qrCode);

    @Query("SELECT s FROM SessionMember s WHERE subscriptionMember.member.onlineUser.id = :memberId")
    List<SessionMember> findSessionByMemberId(Long memberId);



}

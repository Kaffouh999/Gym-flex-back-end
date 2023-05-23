package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.AssuranceMember;
import com.example.GymInTheBack.entities.Member;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the AssuranceMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuranceMemberRepository extends JpaRepository<AssuranceMember, Long> {
    @Query("SELECT a FROM AssuranceMember a " + "WHERE a.startDate <= :endDate " + "AND a.endDate >= :startDate And member.id = :memberId ")
    List<AssuranceMember> findIntersectingAssurances(ZonedDateTime startDate, ZonedDateTime endDate , Long memberId);
}

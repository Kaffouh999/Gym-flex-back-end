package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.SubscriptionMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the SubscriptionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionMemberRepository extends JpaRepository<SubscriptionMember, Long> {
    List<SubscriptionMember> findAll(Sort sort);
    Optional<SubscriptionMember> findByCodeSubscription(String codeSubscription);

    @Query("select e from SubscriptionMember e where codeSubscription = :qrCode and e.endDate is not null and  current_date between e.startDate and e.endDate")
    List<SubscriptionMember> entering(String qrCode);

    @Query("SELECT s FROM SubscriptionMember s WHERE s.member.onlineUser.id = :userId")
    List<SubscriptionMember> findByMemberId(Long userId);

    @Query("SELECT s FROM SubscriptionMember s WHERE (:membeId is null or s.member.id = :membeId) and (:planId is null or s.plan.id = :planId ) and ( CAST(:endDate  AS timestamp) is null or CAST(s.startDate AS timestamp) <= :endDate) and ( CAST(:startDate  AS timestamp) is null or CAST(s.endDate AS timestamp) >= :startDate)")
    List<SubscriptionMember> searchSubscriptions(Long membeId , Long planId, Timestamp startDate,Timestamp endDate);
}

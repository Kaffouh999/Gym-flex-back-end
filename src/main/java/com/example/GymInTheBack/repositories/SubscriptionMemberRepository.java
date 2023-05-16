package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.SubscriptionMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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

}

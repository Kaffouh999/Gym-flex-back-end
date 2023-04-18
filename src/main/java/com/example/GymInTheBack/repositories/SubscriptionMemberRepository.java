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

    @Query("select e from SubscriptionMember e where codeSubscription = :qrCode and date_part('day', age( current_date , e.startDate)) <= e.plan.duration and date_part('day', age( current_date , e.startDate)) >= 0")
    List<SubscriptionMember> entering(String qrCode);



}

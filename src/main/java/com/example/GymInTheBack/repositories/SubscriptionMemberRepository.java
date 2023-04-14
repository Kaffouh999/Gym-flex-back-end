package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.SubscriptionMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the SubscriptionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionMemberRepository extends JpaRepository<SubscriptionMember, Long> {
    List<SubscriptionMember> findAll(Sort sort);
}

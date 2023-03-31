package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.SubscriptionMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscriptionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionMemberRepository extends JpaRepository<SubscriptionMember, Long> {}

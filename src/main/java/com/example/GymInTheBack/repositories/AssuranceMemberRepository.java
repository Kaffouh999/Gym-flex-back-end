package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.AssuranceMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssuranceMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuranceMemberRepository extends JpaRepository<AssuranceMember, Long> {}

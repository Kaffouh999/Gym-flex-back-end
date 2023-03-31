package com.example.GymInTheBack.repositories;


import com.example.GymInTheBack.entities.SessionMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionMemberRepository extends JpaRepository<SessionMember, Long> {}

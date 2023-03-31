package com.example.GymInTheBack.repositories;


import com.example.GymInTheBack.entities.Reform;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReformRepository extends JpaRepository<Reform, Long> {}

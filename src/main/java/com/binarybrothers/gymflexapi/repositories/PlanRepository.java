package com.binarybrothers.gymflexapi.repositories;


import com.binarybrothers.gymflexapi.entities.Plan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Plan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByName(String name);
}

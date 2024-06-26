package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.GymBranch;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GymBranch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GymBranchRepository extends JpaRepository<GymBranch, Long> {
    GymBranch findByName(String name);
}

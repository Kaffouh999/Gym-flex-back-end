package com.example.GymInTheBack.repositories;


import com.example.GymInTheBack.entities.Maintining;
import com.example.GymInTheBack.entities.Reform;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Reform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReformRepository extends JpaRepository<Reform, Long> {
    @Query("SELECT a FROM Reform a " + "WHERE a.item.id = :reformItemId ")
    List<Reform> findOldReform(Long reformItemId);
}

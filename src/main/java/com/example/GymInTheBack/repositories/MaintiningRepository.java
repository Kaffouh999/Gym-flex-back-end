package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.AssuranceMember;
import com.example.GymInTheBack.entities.Maintining;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface MaintiningRepository extends JpaRepository<Maintining, Long> {
    @Query("SELECT a FROM Maintining a " + "WHERE a.startDate <= :endDate " + "AND a.endDate >= :startDate And item.id = :equipmentId ")
    List<Maintining> findIntersectingMaintinings(ZonedDateTime startDate, ZonedDateTime endDate , Long equipmentId);
}

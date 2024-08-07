package com.binarybrothers.gymflexapi.repositories;


import com.binarybrothers.gymflexapi.dtos.statistics.MemberEquipmentStatistic;
import com.binarybrothers.gymflexapi.entities.Member;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Member entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query("SELECT DISTINCT  NEW com.binarybrothers.gymflexapi.dtos.statistics.MemberEquipmentStatistic(" +
            "(SELECT COUNT(m) FROM Member m WHERE m.gender = true), " +
            "(SELECT COUNT(m) FROM Member m WHERE m.gender = false) , " +
            "(SELECT COUNT(ei) FROM EquipmentItem ei) , " +
            "(SELECT COUNT(e) FROM Equipment e) ) " +
            "FROM Member m")
   MemberEquipmentStatistic getMemberEquipmentStatistics();//distinct key world is required

    @Query("SELECT m FROM Member m WHERE m.onlineUser.role.coach = true")
    List<Member> findAllCoachMembers();
}

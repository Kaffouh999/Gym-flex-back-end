package com.binarybrothers.gymflexapi.repositories;


import com.binarybrothers.gymflexapi.dtos.statistics.PaymentStatistics;
import com.binarybrothers.gymflexapi.entities.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.subscriptionMember.member.id = :memberId")
    List<Payment> findByMemberId(Long memberId);

    @Query("SELECT new com.binarybrothers.gymflexapi.dtos.statistics.PaymentStatistics(p, COUNT(py), COUNT(DISTINCT s), SUM(py.amountPayed)) " +
            "FROM Payment py " +
            "JOIN py.subscriptionMember s " +
            "JOIN s.plan p " +
            " GROUP BY p")
    List<PaymentStatistics> getPaymentStatistics();

}

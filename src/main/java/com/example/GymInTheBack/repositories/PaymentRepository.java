package com.example.GymInTheBack.repositories;


import com.example.GymInTheBack.dtos.statistics.PaymentStatistics;
import com.example.GymInTheBack.entities.Payment;
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

    @Query("SELECT new com.example.GymInTheBack.dtos.statistics.PaymentStatistics(p, COUNT(py), COUNT(DISTINCT s), SUM(py.amountPayed)) " +
            "FROM Payment py " +
            "JOIN py.subscriptionMember s " +
            "JOIN s.plan p " +
            " GROUP BY p")
    List<PaymentStatistics> getPaymentStatistics();

}

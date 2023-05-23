package com.example.GymInTheBack.services.statistics;

import com.example.GymInTheBack.dtos.statistics.PaymentStatistics;

import java.util.List;

public interface StatisticsService {
    List<PaymentStatistics> getPaymentStatistics();
}

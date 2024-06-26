package com.binarybrothers.gymflexapi.services.statistics;

import com.binarybrothers.gymflexapi.dtos.statistics.PaymentStatistics;

import java.util.List;

public interface StatisticsService {
    List<PaymentStatistics> getPaymentStatistics();
}

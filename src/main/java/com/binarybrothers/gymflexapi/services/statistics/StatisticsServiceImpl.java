package com.binarybrothers.gymflexapi.services.statistics;

import com.binarybrothers.gymflexapi.dtos.statistics.PaymentStatistics;
import com.binarybrothers.gymflexapi.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
    private final PaymentRepository paymentRepository;

    public StatisticsServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentStatistics> getPaymentStatistics() {
        return paymentRepository.getPaymentStatistics();
    }
}

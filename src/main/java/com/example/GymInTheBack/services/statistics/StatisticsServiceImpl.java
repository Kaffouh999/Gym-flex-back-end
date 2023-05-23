package com.example.GymInTheBack.services.statistics;

import com.example.GymInTheBack.dtos.statistics.PaymentStatistics;
import com.example.GymInTheBack.repositories.PaymentRepository;
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

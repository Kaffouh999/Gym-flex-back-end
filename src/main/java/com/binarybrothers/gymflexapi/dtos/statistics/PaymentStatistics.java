package com.binarybrothers.gymflexapi.dtos.statistics;

import com.binarybrothers.gymflexapi.entities.Plan;

public class PaymentStatistics {
    private Plan plan;
    private Long numberOfPayments;
    private Long numberOfSubscriptions;
    private Double totalAmountPayed;

    public PaymentStatistics(Plan plan, Long numberOfPayments, Long numberOfSubscriptions, Double totalAmountPayed) {
        this.plan = plan;
        this.numberOfPayments = numberOfPayments;
        this.numberOfSubscriptions = numberOfSubscriptions;
        this.totalAmountPayed = totalAmountPayed;
    }

    public Double getTotalAmountPayed() {
        return totalAmountPayed;
    }

    public void setTotalAmountPayed(Double totalAmountPayed) {
        this.totalAmountPayed = totalAmountPayed;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Long getNumberOfPayments() {
        return numberOfPayments;
    }

    public void setNumberOfPayments(Long numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    public Long getNumberOfSubscriptions() {
        return numberOfSubscriptions;
    }

    public void setNumberOfSubscriptions(Long numberOfSubscriptions) {
        this.numberOfSubscriptions = numberOfSubscriptions;
    }
}


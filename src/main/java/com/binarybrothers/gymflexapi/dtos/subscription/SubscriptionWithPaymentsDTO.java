package com.binarybrothers.gymflexapi.dtos.subscription;

import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.plan.PlanDTO;
import com.binarybrothers.gymflexapi.entities.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public class SubscriptionWithPaymentsDTO {
    private Long id;

    @NotNull
    private ZonedDateTime startDate;


    private ZonedDateTime endDate;
    private String codeSubscription;

    private Float discountPercentage;

    private MemberDTO member;

    private PlanDTO plan;

    private List<Payment> paymentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getCodeSubscription() {
        return codeSubscription;
    }

    public void setCodeSubscription(String codeSubscription) {
        this.codeSubscription = codeSubscription;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    @JsonIgnoreProperties(value = "subscriptionMember")
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}

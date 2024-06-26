package com.binarybrothers.gymflexapi.dtos.payment;

import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;


    private Double shouldPay;

    @NotNull
    private Double amountPayed;

    @NotNull
    private ZonedDateTime paymentDate;

    private SubscriptionMemberDTO subscriptionMember;

    private MemberDTO payedMember;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(Double amountPayed) {
        this.amountPayed = amountPayed;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public SubscriptionMemberDTO getSubscriptionMember() {
        return subscriptionMember;
    }

    public void setSubscriptionMember(SubscriptionMemberDTO subscriptionMember) {
        this.subscriptionMember = subscriptionMember;
    }

    public MemberDTO getPayedMember() {
        return payedMember;
    }

    public void setPayedMember(MemberDTO payedMember) {
        this.payedMember = payedMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", amountPayed=" + getAmountPayed() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", subscriptionMember=" + getSubscriptionMember() +
            ", payedMember=" + getPayedMember() +
            "}";
    }
}

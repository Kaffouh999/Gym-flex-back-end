package com.binarybrothers.gymflexapi.dtos.subscription;

import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.plan.PlanDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startDate;


    private ZonedDateTime endDate;
    private String codeSubscription;

    private Float discountPercentage;

    private MemberDTO member;

    private PlanDTO plan;

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

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionMemberDTO)) {
            return false;
        }

        SubscriptionMemberDTO subscriptionMemberDTO = (SubscriptionMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionMemberDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
                ", endDate='" + getEndDate() + "'" +
            ", codeSubscription='" + getCodeSubscription() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            ", member=" + getMember() +
            ", plan=" + getPlan() +
            "}";
    }
}

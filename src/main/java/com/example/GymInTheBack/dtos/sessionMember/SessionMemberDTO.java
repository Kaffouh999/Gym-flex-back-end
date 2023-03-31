package com.example.GymInTheBack.dtos.sessionMember;

import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime enteringTime;

    private ZonedDateTime leavingTime;

    private SubscriptionMemberDTO subscriptionMember;

    private GymBranchDTO gymBranch;

    private MemberDTO managerAtTheTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getEnteringTime() {
        return enteringTime;
    }

    public void setEnteringTime(ZonedDateTime enteringTime) {
        this.enteringTime = enteringTime;
    }

    public ZonedDateTime getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(ZonedDateTime leavingTime) {
        this.leavingTime = leavingTime;
    }

    public SubscriptionMemberDTO getSubscriptionMember() {
        return subscriptionMember;
    }

    public void setSubscriptionMember(SubscriptionMemberDTO subscriptionMember) {
        this.subscriptionMember = subscriptionMember;
    }

    public GymBranchDTO getGymBranch() {
        return gymBranch;
    }

    public void setGymBranch(GymBranchDTO gymBranch) {
        this.gymBranch = gymBranch;
    }

    public MemberDTO getManagerAtTheTime() {
        return managerAtTheTime;
    }

    public void setManagerAtTheTime(MemberDTO managerAtTheTime) {
        this.managerAtTheTime = managerAtTheTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionMemberDTO)) {
            return false;
        }

        SessionMemberDTO sessionMemberDTO = (SessionMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionMemberDTO{" +
            "id=" + getId() +
            ", enteringTime='" + getEnteringTime() + "'" +
            ", leavingTime='" + getLeavingTime() + "'" +
            ", subscriptionMember=" + getSubscriptionMember() +
            ", gymBranch=" + getGymBranch() +
            ", managerAtTheTime=" + getManagerAtTheTime() +
            "}";
    }
}

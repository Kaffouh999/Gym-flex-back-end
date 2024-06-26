package com.binarybrothers.gymflexapi.dtos.assurancemember;

import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssuranceMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amountPayed;

    private String assurancAgency;

    @NotNull
    private ZonedDateTime startDate;

    @NotNull
    private ZonedDateTime endDate;

    private MemberDTO member;

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

    public String getAssurancAgency() {
        return assurancAgency;
    }

    public void setAssurancAgency(String assurancAgency) {
        this.assurancAgency = assurancAgency;
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

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssuranceMemberDTO)) {
            return false;
        }

        AssuranceMemberDTO assuranceMemberDTO = (AssuranceMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assuranceMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssuranceMemberDTO{" +
            "id=" + getId() +
            ", amountPayed=" + getAmountPayed() +
            ", assurancAgency='" + getAssurancAgency() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", member=" + getMember() +
            "}";
    }
}

package com.binarybrothers.gymflexapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.validation.constraints.*;

/**
 * A AssuranceMember.
 */
@Entity
@Table(name = "assurance_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssuranceMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount_payed", nullable = false)
    private Double amountPayed;

    @Column(name = "assuranc_agency")
    private String assurancAgency;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssuranceMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountPayed() {
        return this.amountPayed;
    }

    public AssuranceMember amountPayed(Double amountPayed) {
        this.setAmountPayed(amountPayed);
        return this;
    }

    public void setAmountPayed(Double amountPayed) {
        this.amountPayed = amountPayed;
    }

    public String getAssurancAgency() {
        return this.assurancAgency;
    }

    public AssuranceMember assurancAgency(String assurancAgency) {
        this.setAssurancAgency(assurancAgency);
        return this;
    }

    public void setAssurancAgency(String assurancAgency) {
        this.assurancAgency = assurancAgency;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public AssuranceMember startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public AssuranceMember endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public AssuranceMember member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssuranceMember)) {
            return false;
        }
        return id != null && id.equals(((AssuranceMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssuranceMember{" +
            "id=" + getId() +
            ", amountPayed=" + getAmountPayed() +
            ", assurancAgency='" + getAssurancAgency() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}

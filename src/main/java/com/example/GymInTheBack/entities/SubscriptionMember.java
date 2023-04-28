package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A SubscriptionMember.
 */
@Entity
@Table(name = "subscription_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;


    @Column(name = "start_end", nullable = true)
    private ZonedDateTime endDate;

    @Column(name = "code_subscription")
    private String codeSubscription;

    @Column(name = "discount_percentage")
    private Float discountPercentage;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member member;

    @ManyToOne(optional = false)
    @NotNull
    private Plan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public SubscriptionMember startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public String getCodeSubscription() {
        return this.codeSubscription;
    }

    public SubscriptionMember codeSubscription(String codeSubscription) {
        this.setCodeSubscription(codeSubscription);
        return this;
    }

    public void setCodeSubscription(String codeSubscription) {
        this.codeSubscription = codeSubscription;
    }

    public Float getDiscountPercentage() {
        return this.discountPercentage;
    }

    public SubscriptionMember discountPercentage(Float discountPercentage) {
        this.setDiscountPercentage(discountPercentage);
        return this;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public SubscriptionMember member(Member member) {
        this.setMember(member);
        return this;
    }

    public Plan getPlan() {
        return this.plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public SubscriptionMember plan(Plan plan) {
        this.setPlan(plan);
        return this;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
    public SubscriptionMember endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionMember)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionMember{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
                ", endDate='" + getEndDate() + "'" +
            ", codeSubscription='" + getCodeSubscription() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            "}";
    }
}

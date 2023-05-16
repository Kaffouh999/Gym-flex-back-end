package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "should_pay")
    private Double shouldPay;

    @NotNull
    @Column(name = "amount_payed", nullable = false)
    private Double amountPayed;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private ZonedDateTime paymentDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "member", "plan" }, allowSetters = true)
    private SubscriptionMember subscriptionMember;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member payedMember;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmountPayed() {
        return this.amountPayed;
    }

    public Payment amountPayed(Double amountPayed) {
        this.setAmountPayed(amountPayed);
        return this;
    }

    public void setAmountPayed(Double amountPayed) {
        this.amountPayed = amountPayed;
    }

    public ZonedDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(ZonedDateTime paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public SubscriptionMember getSubscriptionMember() {
        return this.subscriptionMember;
    }

    public void setSubscriptionMember(SubscriptionMember subscriptionMember) {
        this.subscriptionMember = subscriptionMember;
    }

    public Payment subscriptionMember(SubscriptionMember subscriptionMember) {
        this.setSubscriptionMember(subscriptionMember);
        return this;
    }

    public Member getPayedMember() {
        return this.payedMember;
    }

    public void setPayedMember(Member member) {
        this.payedMember = member;
    }

    public Payment payedMember(Member member) {
        this.setPayedMember(member);
        return this;
    }
    public Payment shouldPay(Double shouldPay) {
        this.setShouldPay(shouldPay);
        return this;
    }

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amountPayed=" + getAmountPayed() +
            ", paymentDate='" + getPaymentDate() + "'" +
            "}";
    }
}

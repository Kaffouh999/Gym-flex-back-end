package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A SessionMember.
 */
@Entity
@Table(name = "session_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "entering_time", nullable = false)
    private ZonedDateTime enteringTime;

    @Column(name = "leaving_time")
    private ZonedDateTime leavingTime;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = { "member", "plan" }, allowSetters = true)
    private SubscriptionMember subscriptionMember;

    @ManyToOne(optional = false)
    @NotNull
    private GymBranch gymBranch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member managerAtTheTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getEnteringTime() {
        return this.enteringTime;
    }

    public SessionMember enteringTime(ZonedDateTime enteringTime) {
        this.setEnteringTime(enteringTime);
        return this;
    }

    public void setEnteringTime(ZonedDateTime enteringTime) {
        this.enteringTime = enteringTime;
    }

    public ZonedDateTime getLeavingTime() {
        return this.leavingTime;
    }

    public SessionMember leavingTime(ZonedDateTime leavingTime) {
        this.setLeavingTime(leavingTime);
        return this;
    }

    public void setLeavingTime(ZonedDateTime leavingTime) {
        this.leavingTime = leavingTime;
    }

    public SubscriptionMember getSubscriptionMember() {
        return this.subscriptionMember;
    }

    public void setSubscriptionMember(SubscriptionMember subscriptionMember) {
        this.subscriptionMember = subscriptionMember;
    }

    public SessionMember subscriptionMember(SubscriptionMember subscriptionMember) {
        this.setSubscriptionMember(subscriptionMember);
        return this;
    }

    public GymBranch getGymBranch() {
        return this.gymBranch;
    }

    public void setGymBranch(GymBranch gymBranch) {
        this.gymBranch = gymBranch;
    }

    public SessionMember gymBranch(GymBranch gymBranch) {
        this.setGymBranch(gymBranch);
        return this;
    }

    public Member getManagerAtTheTime() {
        return this.managerAtTheTime;
    }

    public void setManagerAtTheTime(Member member) {
        this.managerAtTheTime = member;
    }

    public SessionMember managerAtTheTime(Member member) {
        this.setManagerAtTheTime(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionMember)) {
            return false;
        }
        return id != null && id.equals(((SessionMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionMember{" +
            "id=" + getId() +
            ", enteringTime='" + getEnteringTime() + "'" +
            ", leavingTime='" + getLeavingTime() + "'" +
            "}";
    }
}

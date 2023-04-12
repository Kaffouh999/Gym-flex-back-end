package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;


@Entity
@Table(name = "reform")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reform implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "decision_date")
    private ZonedDateTime decisionDate;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipment", "gymBranch" }, allowSetters = true)
    private EquipmentItem item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member decider;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reform id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDecisionDate() {
        return this.decisionDate;
    }

    public Reform decisionDate(ZonedDateTime decisionDate) {
        this.setDecisionDate(decisionDate);
        return this;
    }

    public void setDecisionDate(ZonedDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getComment() {
        return this.comment;
    }

    public Reform comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EquipmentItem getItem() {
        return this.item;
    }

    public void setItem(EquipmentItem equipmentItem) {
        this.item = equipmentItem;
    }

    public Reform item(EquipmentItem equipmentItem) {
        this.setItem(equipmentItem);
        return this;
    }

    public Member getDecider() {
        return this.decider;
    }

    public void setDecider(Member member) {
        this.decider = member;
    }

    public Reform decider(Member member) {
        this.setDecider(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reform)) {
            return false;
        }
        return id != null && id.equals(((Reform) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reform{" +
            "id=" + getId() +
            ", decisionDate='" + getDecisionDate() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}

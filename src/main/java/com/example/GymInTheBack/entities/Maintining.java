package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;


@Entity
@Table(name = "maintining")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Maintining implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "equipment", "gymBranch" }, allowSetters = true)
    private EquipmentItem item;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gymBranch" }, allowSetters = true)
    private Member maintainerResponsible;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maintining id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Maintining startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public Maintining endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Double getCost() {
        return this.cost;
    }

    public Maintining cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public EquipmentItem getItem() {
        return this.item;
    }

    public void setItem(EquipmentItem equipmentItem) {
        this.item = equipmentItem;
    }

    public Maintining item(EquipmentItem equipmentItem) {
        this.setItem(equipmentItem);
        return this;
    }

    public Member getMaintainerResponsible() {
        return this.maintainerResponsible;
    }

    public void setMaintainerResponsible(Member member) {
        this.maintainerResponsible = member;
    }

    public Maintining maintainerResponsible(Member member) {
        this.setMaintainerResponsible(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maintining)) {
            return false;
        }
        return id != null && id.equals(((Maintining) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maintining{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}

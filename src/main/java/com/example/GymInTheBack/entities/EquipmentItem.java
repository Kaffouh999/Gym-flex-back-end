package com.example.GymInTheBack.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;


@Entity
@Table(name = "equipment_item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipmentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_use_date")
    private ZonedDateTime firstUseDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "amortization")
    private Double amortization;

    @Column(name = "bare_code")
    private String bareCode;

    @Column(name = "safe_min_value")
    private Double safeMinValue;

    @ManyToOne(optional = false)
    @NotNull
    private Equipment equipment;

    @ManyToOne(optional = false)
    @NotNull
    private GymBranch gymBranch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EquipmentItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFirstUseDate() {
        return this.firstUseDate;
    }

    public EquipmentItem firstUseDate(ZonedDateTime firstUseDate) {
        this.setFirstUseDate(firstUseDate);
        return this;
    }

    public void setFirstUseDate(ZonedDateTime firstUseDate) {
        this.firstUseDate = firstUseDate;
    }

    public Double getPrice() {
        return this.price;
    }

    public EquipmentItem price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmortization() {
        return this.amortization;
    }

    public EquipmentItem amortization(Double amortization) {
        this.setAmortization(amortization);
        return this;
    }

    public void setAmortization(Double amortization) {
        this.amortization = amortization;
    }

    public String getBareCode() {
        return this.bareCode;
    }

    public EquipmentItem bareCode(String bareCode) {
        this.setBareCode(bareCode);
        return this;
    }

    public void setBareCode(String bareCode) {
        this.bareCode = bareCode;
    }

    public Double getSafeMinValue() {
        return this.safeMinValue;
    }

    public EquipmentItem safeMinValue(Double safeMinValue) {
        this.setSafeMinValue(safeMinValue);
        return this;
    }

    public void setSafeMinValue(Double safeMinValue) {
        this.safeMinValue = safeMinValue;
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public EquipmentItem equipment(Equipment equipment) {
        this.setEquipment(equipment);
        return this;
    }

    public GymBranch getGymBranch() {
        return this.gymBranch;
    }

    public void setGymBranch(GymBranch gymBranch) {
        this.gymBranch = gymBranch;
    }

    public EquipmentItem gymBranch(GymBranch gymBranch) {
        this.setGymBranch(gymBranch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentItem)) {
            return false;
        }
        return id != null && id.equals(((EquipmentItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentItem{" +
            "id=" + getId() +
            ", firstUseDate='" + getFirstUseDate() + "'" +
            ", price=" + getPrice() +
            ", amortization=" + getAmortization() +
            ", bareCode='" + getBareCode() + "'" +
            ", safeMinValue=" + getSafeMinValue() +
            "}";
    }
}

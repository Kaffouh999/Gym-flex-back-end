package com.example.GymInTheBack.dtos.equipmentItem;

import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipmentItemDTO implements Serializable {

    private Long id;

    private ZonedDateTime firstUseDate;

    private Double price;

    private Double amortization;

    private String bareCode;

    private Double safeMinValue;

    private EquipmentDTO equipment;

    private GymBranchDTO gymBranch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFirstUseDate() {
        return firstUseDate;
    }

    public void setFirstUseDate(ZonedDateTime firstUseDate) {
        this.firstUseDate = firstUseDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmortization() {
        return amortization;
    }

    public void setAmortization(Double amortization) {
        this.amortization = amortization;
    }

    public String getBareCode() {
        return bareCode;
    }

    public void setBareCode(String bareCode) {
        this.bareCode = bareCode;
    }

    public Double getSafeMinValue() {
        return safeMinValue;
    }

    public void setSafeMinValue(Double safeMinValue) {
        this.safeMinValue = safeMinValue;
    }

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    public GymBranchDTO getGymBranch() {
        return gymBranch;
    }

    public void setGymBranch(GymBranchDTO gymBranch) {
        this.gymBranch = gymBranch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentItemDTO)) {
            return false;
        }

        EquipmentItemDTO equipmentItemDTO = (EquipmentItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipmentItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentItemDTO{" +
            "id=" + getId() +
            ", firstUseDate='" + getFirstUseDate() + "'" +
            ", price=" + getPrice() +
            ", amortization=" + getAmortization() +
            ", bareCode='" + getBareCode() + "'" +
            ", safeMinValue=" + getSafeMinValue() +
            ", equipment=" + getEquipment() +
            ", gymBranch=" + getGymBranch() +
            "}";
    }
}

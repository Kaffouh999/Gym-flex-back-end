package com.binarybrothers.gymflexapi.dtos.maintining;

import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;
import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintiningDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Double cost;

    private EquipmentItemDTO item;

    private MemberDTO maintainerResponsible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public EquipmentItemDTO getItem() {
        return item;
    }

    public void setItem(EquipmentItemDTO item) {
        this.item = item;
    }

    public MemberDTO getMaintainerResponsible() {
        return maintainerResponsible;
    }

    public void setMaintainerResponsible(MemberDTO maintainerResponsible) {
        this.maintainerResponsible = maintainerResponsible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintiningDTO)) {
            return false;
        }

        MaintiningDTO maintiningDTO = (MaintiningDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintiningDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintiningDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", cost=" + getCost() +
            ", item=" + getItem() +
            ", maintainerResponsible=" + getMaintainerResponsible() +
            "}";
    }
}

package com.example.GymInTheBack.dtos.reform;

import com.example.GymInTheBack.dtos.equipmentItem.EquipmentItemDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReformDTO implements Serializable {

    private Long id;

    private ZonedDateTime decisionDate;

    private String comment;

    private EquipmentItemDTO item;

    private MemberDTO decider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(ZonedDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EquipmentItemDTO getItem() {
        return item;
    }

    public void setItem(EquipmentItemDTO item) {
        this.item = item;
    }

    public MemberDTO getDecider() {
        return decider;
    }

    public void setDecider(MemberDTO decider) {
        this.decider = decider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReformDTO)) {
            return false;
        }

        ReformDTO reformDTO = (ReformDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reformDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReformDTO{" +
            "id=" + getId() +
            ", decisionDate='" + getDecisionDate() + "'" +
            ", comment='" + getComment() + "'" +
            ", item=" + getItem() +
            ", decider=" + getDecider() +
            "}";
    }
}

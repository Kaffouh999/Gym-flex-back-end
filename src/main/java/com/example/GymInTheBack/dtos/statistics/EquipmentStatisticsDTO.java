package com.example.GymInTheBack.dtos.statistics;

public class EquipmentStatisticsDTO {
    private String equipmentName;
    private Long equipmentItemCount;

    public EquipmentStatisticsDTO(String equipmentName, Long equipmentItemCount) {
        this.equipmentName = equipmentName;
        this.equipmentItemCount = equipmentItemCount;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Long getEquipmentItemCount() {
        return equipmentItemCount;
    }

    public void setEquipmentItemCount(Long equipmentItemCount) {
        this.equipmentItemCount = equipmentItemCount;
    }
}

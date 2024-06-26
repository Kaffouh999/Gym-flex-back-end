package com.binarybrothers.gymflexapi.dtos.statistics;

public class MemberEquipmentStatistic {
    private Long numberOfMen;
    private Long numberOfWomen;
    private Long numberOfEquipmentItems;
    private Long numberOfEquipments;

    public MemberEquipmentStatistic(Long numberOfMen, Long numberOfWomen, Long numberOfEquipmentItems, Long numberOfEquipments) {
        this.numberOfMen = numberOfMen;
        this.numberOfWomen = numberOfWomen;
        this.numberOfEquipmentItems = numberOfEquipmentItems;
        this.numberOfEquipments = numberOfEquipments;
    }

    public Long getNumberOfMen() {
        return numberOfMen;
    }

    public void setNumberOfMen(Long numberOfMen) {
        this.numberOfMen = numberOfMen;
    }

    public Long getNumberOfWomen() {
        return numberOfWomen;
    }

    public void setNumberOfWomen(Long numberOfWomen) {
        this.numberOfWomen = numberOfWomen;
    }

    public Long getNumberOfEquipmentItems() {
        return numberOfEquipmentItems;
    }

    public void setNumberOfEquipmentItems(Long numberOfEquipmentItems) {
        this.numberOfEquipmentItems = numberOfEquipmentItems;
    }

    public Long getNumberOfEquipments() {
        return numberOfEquipments;
    }

    public void setNumberOfEquipments(Long numberOfEquipments) {
        this.numberOfEquipments = numberOfEquipments;
    }
}

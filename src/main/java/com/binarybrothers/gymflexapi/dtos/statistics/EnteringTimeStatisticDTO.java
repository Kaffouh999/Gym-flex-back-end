package com.binarybrothers.gymflexapi.dtos.statistics;

public class EnteringTimeStatisticDTO {
   private String range;
   private Double averageMen;
   private Double averageWomen;

    public EnteringTimeStatisticDTO() {
    }

    public EnteringTimeStatisticDTO(String range, Double averageMen, Double averageWomen) {
        this.range = range;
        this.averageMen = averageMen;
        this.averageWomen = averageWomen;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Double getAverageMen() {
        return averageMen;
    }

    public void setAverageMen(Double averageMen) {
        this.averageMen = averageMen;
    }

    public Double getAverageWomen() {
        return averageWomen;
    }

    public void setAverageWomen(Double averageWomen) {
        this.averageWomen = averageWomen;
    }
}

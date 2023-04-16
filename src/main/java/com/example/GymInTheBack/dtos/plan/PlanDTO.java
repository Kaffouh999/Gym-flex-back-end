package com.example.GymInTheBack.dtos.plan;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Long duration;

    @NotNull
    private Double price;


    private String imageAds;


    private Float ratingPer5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageAds() {
        return imageAds;
    }

    public void setImageAds(String imageAds) {
        this.imageAds = imageAds;
    }

    public Float getRatingPer5() {
        return ratingPer5;
    }

    public void setRatingPer5(Float ratingPer5) {
        this.ratingPer5 = ratingPer5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanDTO)) {
            return false;
        }

        PlanDTO planDTO = (PlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", duration=" + getDuration() +
            ", price=" + getPrice() +
                ", imageAds=" + getImageAds() +
                ", ratingPer5=" + getRatingPer5() +
            "}";
    }
}

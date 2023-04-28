package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;


    @Column(name = "imageAds", nullable = true)
    private String imageAds;


    @Column(name = "ratingPer5", nullable = true)
    private Float ratingPer5;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SubscriptionMember> subscriptionMemberList;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Plan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Plan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Plan duration(Long duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return this.price;
    }

    public Plan price(Double price) {
        this.setPrice(price);
        return this;
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
    public Plan price(String imageAds) {
        this.setImageAds(imageAds);
        return this;
    }

    public Float getRatingPer5() {
        return ratingPer5;
    }

    public void setRatingPer5(Float ratingPer5) {
        this.ratingPer5 = ratingPer5;
    }

    public Plan price(Float ratingPer5) {
        this.setRatingPer5(ratingPer5);
        return this;
    }

    public List<SubscriptionMember> getSubscriptionMemberList() {
        return subscriptionMemberList;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return id != null && id.equals(((Plan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plan{" +
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

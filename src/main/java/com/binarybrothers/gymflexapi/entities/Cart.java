package com.binarybrothers.gymflexapi.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;


@Entity
@Table(name = "cart")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "shipping_adress", nullable = false)
    private String shippingAdress;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "shipped")
    private Boolean shipped;

    @Column(name = "ordering_date")
    private ZonedDateTime orderingDate;

    @Column(name = "shipping_date")
    private ZonedDateTime shippingDate;

    @ManyToOne(optional = false)
    @NotNull
    private OnlineUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShippingAdress() {
        return this.shippingAdress;
    }

    public Cart shippingAdress(String shippingAdress) {
        this.setShippingAdress(shippingAdress);
        return this;
    }

    public void setShippingAdress(String shippingAdress) {
        this.shippingAdress = shippingAdress;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Cart paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getShipped() {
        return this.shipped;
    }

    public Cart shipped(Boolean shipped) {
        this.setShipped(shipped);
        return this;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public ZonedDateTime getOrderingDate() {
        return this.orderingDate;
    }

    public Cart orderingDate(ZonedDateTime orderingDate) {
        this.setOrderingDate(orderingDate);
        return this;
    }

    public void setOrderingDate(ZonedDateTime orderingDate) {
        this.orderingDate = orderingDate;
    }

    public ZonedDateTime getShippingDate() {
        return this.shippingDate;
    }

    public Cart shippingDate(ZonedDateTime shippingDate) {
        this.setShippingDate(shippingDate);
        return this;
    }

    public void setShippingDate(ZonedDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public OnlineUser getUser() {
        return this.user;
    }

    public void setUser(OnlineUser user) {
        this.user = user;
    }

    public Cart user(OnlineUser user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", shippingAdress='" + getShippingAdress() + "'" +
            ", paid='" + getPaid() + "'" +
            ", shipped='" + getShipped() + "'" +
            ", orderingDate='" + getOrderingDate() + "'" +
            ", shippingDate='" + getShippingDate() + "'" +
            "}";
    }
}

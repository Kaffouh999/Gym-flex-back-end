package com.example.GymInTheBack.dtos.cart;


import com.example.GymInTheBack.dtos.user.OnlineUserDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartDTO implements Serializable {

    private Long id;

    @NotNull
    private String shippingAdress;

    private Boolean paid;

    private Boolean shipped;

    private ZonedDateTime orderingDate;

    private ZonedDateTime shippingDate;

    private OnlineUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShippingAdress() {
        return shippingAdress;
    }

    public void setShippingAdress(String shippingAdress) {
        this.shippingAdress = shippingAdress;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public ZonedDateTime getOrderingDate() {
        return orderingDate;
    }

    public void setOrderingDate(ZonedDateTime orderingDate) {
        this.orderingDate = orderingDate;
    }

    public ZonedDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(ZonedDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public OnlineUserDTO getUser() {
        return user;
    }

    public void setUser(OnlineUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartDTO)) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartDTO{" +
            "id=" + getId() +
            ", shippingAdress='" + getShippingAdress() + "'" +
            ", paid='" + getPaid() + "'" +
            ", shipped='" + getShipped() + "'" +
            ", orderingDate='" + getOrderingDate() + "'" +
            ", shippingDate='" + getShippingDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}

package com.example.GymInTheBack.dtos.order;

import com.example.GymInTheBack.dtos.cart.CartDTO;
import com.example.GymInTheBack.dtos.product.ProductDTO;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Double quantity;

    private ProductDTO product;

    private CartDTO cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", product=" + getProduct() +
            ", cart=" + getCart() +
            "}";
    }
}

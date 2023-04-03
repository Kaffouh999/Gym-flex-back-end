package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.cart.CartDTO;
import com.example.GymInTheBack.dtos.order.OrderDTO;
import com.example.GymInTheBack.dtos.product.ProductDTO;
import com.example.GymInTheBack.entities.Cart;
import com.example.GymInTheBack.entities.Order;
import com.example.GymInTheBack.entities.Product;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "cart", source = "cart", qualifiedByName = "cartId")
    OrderDTO toDto(Order s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "subCategory", source = "subCategory")

    ProductDTO toDtoProductId(Product product);

    @Named("cartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "shippingAdress", source = "shippingAdress")
    @Mapping(target = "paid", source = "paid")
    @Mapping(target = "shipped", source = "shipped")
    @Mapping(target = "orderingDate", source = "orderingDate")
    @Mapping(target = "shippingDate", source = "shippingDate")
    @Mapping(target = "user", source = "user")

    CartDTO toDtoCartId(Cart cart);
}

package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.cart.CartDTO;
import com.binarybrothers.gymflexapi.dtos.order.OrderDTO;
import com.binarybrothers.gymflexapi.dtos.product.ProductDTO;
import com.binarybrothers.gymflexapi.entities.Cart;
import com.binarybrothers.gymflexapi.entities.Order;
import com.binarybrothers.gymflexapi.entities.Product;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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
    @Mapping(target = "subcategory", source = "subcategory")

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

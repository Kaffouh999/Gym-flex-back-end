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
    ProductDTO toDtoProductId(Product product);

    @Named("cartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CartDTO toDtoCartId(Cart cart);
}

package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.cart.CartDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.Cart;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CartDTO toDto(Cart s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    OnlineUserDTO toDtoUserLogin(OnlineUser user);
}

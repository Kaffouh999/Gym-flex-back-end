package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.cart.CartDTO;
import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.binarybrothers.gymflexapi.entities.Cart;
import com.binarybrothers.gymflexapi.entities.OnlineUser;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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

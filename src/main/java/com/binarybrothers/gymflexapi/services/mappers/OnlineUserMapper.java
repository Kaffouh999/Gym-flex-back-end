package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.binarybrothers.gymflexapi.entities.OnlineUser;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface OnlineUserMapper extends EntityMapper<OnlineUserDTO, OnlineUser> {
    @Mapping(source = "id", target = "id")
    OnlineUserDTO toDto(OnlineUser entity);

    @Mapping(source = "id", target = "id")
    OnlineUser toEntity(OnlineUserDTO dto);

}

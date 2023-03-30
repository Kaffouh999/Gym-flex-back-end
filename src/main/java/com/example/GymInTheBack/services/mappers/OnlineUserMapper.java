package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface OnlineUserMapper extends EntityMapper<OnlineUserDTO, OnlineUser> {
    @Mapping(target = "gymBranch", source = "gymBranch", qualifiedByName = "gymBranchId")
    OnlineUserDTO toDto(OnlineUser s);

    @Named("gymBranchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GymBranchDTO toDtoGymBranchId(GymBranch gymBranch);
}

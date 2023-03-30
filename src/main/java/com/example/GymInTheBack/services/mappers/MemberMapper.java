package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "gymBranch", source = "gymBranch", qualifiedByName = "gymBranchId")
    MemberDTO toDto(Member s);

    @Named("gymBranchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GymBranchDTO toDtoGymBranchId(GymBranch gymBranch);
}

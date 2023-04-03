package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.assuranceMember.AssuranceMemberDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.entities.AssuranceMember;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface AssuranceMemberMapper extends EntityMapper<AssuranceMemberDTO, AssuranceMember> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    AssuranceMemberDTO toDto(AssuranceMember s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cin", source = "cin")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "adress", source = "adress")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "gymBranch", source = "gymBranch")
    MemberDTO toDtoMemberId(Member member);
}

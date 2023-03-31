package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.SessionMember;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface SessionMemberMapper extends EntityMapper<SessionMemberDTO, SessionMember> {
    @Mapping(target = "subscriptionMember", source = "subscriptionMember", qualifiedByName = "subscriptionMemberId")
    @Mapping(target = "gymBranch", source = "gymBranch", qualifiedByName = "gymBranchId")
    @Mapping(target = "managerAtTheTime", source = "managerAtTheTime", qualifiedByName = "memberId")
    SessionMemberDTO toDto(SessionMember s);

    @Named("subscriptionMemberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionMemberDTO toDtoSubscriptionMemberId(SubscriptionMember subscriptionMember);

    @Named("gymBranchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GymBranchDTO toDtoGymBranchId(GymBranch gymBranch);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}

package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.sessionMember.SessionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;
import com.binarybrothers.gymflexapi.entities.GymBranch;
import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.entities.SessionMember;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "codeSubscription", source = "codeSubscription")
    @Mapping(target = "discountPercentage", source = "discountPercentage")
    @Mapping(target = "member", source = "member")
    @Mapping(target = "plan", source = "plan")
    @Mapping(target = "endDate", source = "endDate")
    SubscriptionMemberDTO toDtoSubscriptionMemberId(SubscriptionMember subscriptionMember);

    @Named("gymBranchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "adress", source = "adress")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "openingDate", source = "openingDate")
    @Mapping(target = "closingDate", source = "closingDate")
    @Mapping(target = "sessionDurationAllowed", source = "sessionDurationAllowed")
    GymBranchDTO toDtoGymBranchId(GymBranch gymBranch);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cin", source = "cin")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "adress", source = "adress")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "gymBranch", source = "gymBranch")
    @Mapping(target = "onlineUser", source = "onlineUser")
    MemberDTO toDtoMemberId(Member member);
}

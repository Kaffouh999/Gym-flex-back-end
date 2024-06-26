package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.dtos.plan.PlanDTO;
import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.entities.Plan;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface SubscriptionMemberWithPaymentsMapper extends EntityMapper<SubscriptionWithPaymentsDTO, SubscriptionMember> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "planId")
    SubscriptionWithPaymentsDTO toDto(SubscriptionMember s);

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

    @Named("planId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "imageAds", source = "imageAds")
    @Mapping(target = "ratingPer5", source = "ratingPer5")
    PlanDTO toDtoPlanId(Plan plan);
}

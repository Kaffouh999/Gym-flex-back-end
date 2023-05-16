package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.plan.PlanDTO;
import com.example.GymInTheBack.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.utils.EntityMapper;
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

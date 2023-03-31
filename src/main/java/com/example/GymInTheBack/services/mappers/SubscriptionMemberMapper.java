package com.example.GymInTheBack.services.mappers;

import com.example.GymInTheBack.dtos.SubscriptionMemberDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.plan.PlanDTO;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface SubscriptionMemberMapper extends EntityMapper<SubscriptionMemberDTO, SubscriptionMember> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "planId")
    SubscriptionMemberDTO toDto(SubscriptionMember s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);

    @Named("planId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanDTO toDtoPlanId(Plan plan);
}

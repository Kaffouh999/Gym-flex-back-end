package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.payment.PaymentDTO;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Payment;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "subscriptionMember", source = "subscriptionMember", qualifiedByName = "subscriptionMemberId")
    @Mapping(target = "payedMember", source = "payedMember", qualifiedByName = "memberId")
    PaymentDTO toDto(Payment s);

    @Named("subscriptionMemberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionMemberDTO toDtoSubscriptionMemberId(SubscriptionMember subscriptionMember);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}

package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.equipmentItem.EquipmentItemDTO;
import com.example.GymInTheBack.dtos.member.MemberDTO;
import com.example.GymInTheBack.dtos.reform.ReformDTO;
import com.example.GymInTheBack.entities.EquipmentItem;
import com.example.GymInTheBack.entities.Member;
import com.example.GymInTheBack.entities.Reform;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ReformMapper extends EntityMapper<ReformDTO, Reform> {
    @Mapping(target = "item", source = "item", qualifiedByName = "equipmentItemId")
    @Mapping(target = "decider", source = "decider", qualifiedByName = "memberId")
    ReformDTO toDto(Reform s);

    @Named("equipmentItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstUseDate",source = "firstUseDate")
    @Mapping(target = "price",source = "price")
    @Mapping(target = "amortization",source = "amortization")
    @Mapping(target = "bareCode",source = "bareCode")
    @Mapping(target = "safeMinValue",source = "safeMinValue")
    @Mapping(target = "equipment",source = "equipment")
    @Mapping(target = "gymBranch",source = "gymBranch")

    EquipmentItemDTO toDtoEquipmentItemId(EquipmentItem equipmentItem);

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

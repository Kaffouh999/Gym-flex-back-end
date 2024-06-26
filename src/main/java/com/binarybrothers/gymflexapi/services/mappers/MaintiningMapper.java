package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;
import com.binarybrothers.gymflexapi.dtos.maintining.MaintiningDTO;
import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.entities.EquipmentItem;
import com.binarybrothers.gymflexapi.entities.Maintining;
import com.binarybrothers.gymflexapi.entities.Member;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface MaintiningMapper extends EntityMapper<MaintiningDTO, Maintining> {
    @Mapping(target = "item", source = "item", qualifiedByName = "equipmentItemId")
    @Mapping(target = "maintainerResponsible", source = "maintainerResponsible", qualifiedByName = "memberId")
    MaintiningDTO toDto(Maintining s);

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

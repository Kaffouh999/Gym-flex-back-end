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
    EquipmentItemDTO toDtoEquipmentItemId(EquipmentItem equipmentItem);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}

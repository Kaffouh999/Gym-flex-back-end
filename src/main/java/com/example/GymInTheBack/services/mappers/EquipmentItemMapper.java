package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.dtos.equipmentItem.EquipmentItemDTO;
import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.EquipmentItem;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface EquipmentItemMapper extends EntityMapper<EquipmentItemDTO, EquipmentItem> {
    @Mapping(target = "equipment", source = "equipment", qualifiedByName = "equipmentId")
    @Mapping(target = "gymBranch", source = "gymBranch", qualifiedByName = "gymBranchId")
    EquipmentItemDTO toDto(EquipmentItem s);

    @Named("equipmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EquipmentDTO toDtoEquipmentId(Equipment equipment);

    @Named("gymBranchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GymBranchDTO toDtoGymBranchId(GymBranch gymBranch);
}

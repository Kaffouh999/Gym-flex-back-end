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
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "subCategory", source = "subCategory")
    EquipmentDTO toDtoEquipmentId(Equipment equipment);

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
}

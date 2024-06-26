package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.equipment.EquipmentDTO;
import com.binarybrothers.gymflexapi.dtos.equipmentItem.EquipmentItemDTO;
import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.entities.EquipmentItem;
import com.binarybrothers.gymflexapi.entities.GymBranch;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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
    @Mapping(target = "subcategory", source = "subcategory")
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

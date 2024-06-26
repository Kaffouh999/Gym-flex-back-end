package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.equipment.EquipmentDTO;
import com.binarybrothers.gymflexapi.dtos.subcategory.SubCategoryDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import com.binarybrothers.gymflexapi.entities.SubCategory;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "subCategoryId")
    EquipmentDTO toDto(Equipment s);

    @Named("subCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", source = "category")
    SubCategoryDTO toDtoSubCategoryId(SubCategory subCategory);
}

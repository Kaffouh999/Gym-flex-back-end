package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.equipment.EquipmentDTO;
import com.example.GymInTheBack.dtos.subCategory.SubCategoryDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.SubCategory;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryId")
    EquipmentDTO toDto(Equipment s);

    @Named("subCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", source = "category")
    SubCategoryDTO toDtoSubCategoryId(SubCategory subCategory);
}

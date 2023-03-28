package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.category.CategoryDTO;
import com.example.GymInTheBack.dtos.subCategory.SubCategoryDTO;
import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.SubCategory;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface SubCategoryMapper extends EntityMapper<SubCategoryDTO, SubCategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    SubCategoryDTO toDto(SubCategory s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "isForInventory", source = "isForInventory")
    @Mapping(target = "isForClient", source = "isForClient")

    CategoryDTO toDtoCategoryId(Category category);
}

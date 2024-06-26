package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.category.CategoryDTO;
import com.binarybrothers.gymflexapi.dtos.subcategory.SubCategoryDTO;
import com.binarybrothers.gymflexapi.entities.Category;
import com.binarybrothers.gymflexapi.entities.SubCategory;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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

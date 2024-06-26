package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.category.CategoryDTO;
import com.binarybrothers.gymflexapi.entities.Category;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}

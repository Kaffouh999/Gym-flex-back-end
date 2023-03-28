package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.category.CategoryDTO;
import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {}

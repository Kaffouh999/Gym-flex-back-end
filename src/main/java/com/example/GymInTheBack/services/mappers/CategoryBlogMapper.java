package com.example.GymInTheBack.services.mappers;

import com.example.GymInTheBack.dtos.blogs.CategoryBlogDTO;
import com.example.GymInTheBack.entities.CategoryBlog;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CategoryBlogMapper extends EntityMapper<CategoryBlogDTO, CategoryBlog> {}

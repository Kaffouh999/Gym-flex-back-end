package com.binarybrothers.gymflexapi.services.mappers;

import com.binarybrothers.gymflexapi.dtos.blogs.CategoryBlogDTO;
import com.binarybrothers.gymflexapi.entities.CategoryBlog;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface CategoryBlogMapper extends EntityMapper<CategoryBlogDTO, CategoryBlog> {}

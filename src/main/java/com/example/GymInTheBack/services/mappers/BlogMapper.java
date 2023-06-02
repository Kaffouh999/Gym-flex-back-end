package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.blogs.BlogDTO;
import com.example.GymInTheBack.dtos.blogs.CategoryBlogDTO;
import com.example.GymInTheBack.entities.Blog;
import com.example.GymInTheBack.entities.CategoryBlog;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface BlogMapper extends EntityMapper<BlogDTO, Blog> {
    @Mapping(target = "categoryBlog", source = "categoryBlog", qualifiedByName = "categoryBlogId")
    BlogDTO toDto(Blog s);

    @Named("categoryBlogId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryBlogDTO toDtoCategoryBlogId(CategoryBlog categoryBlog);
}

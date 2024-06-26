package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.blogs.BlogDTO;
import com.binarybrothers.gymflexapi.dtos.blogs.CategoryBlogDTO;
import com.binarybrothers.gymflexapi.entities.Blog;
import com.binarybrothers.gymflexapi.entities.CategoryBlog;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
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

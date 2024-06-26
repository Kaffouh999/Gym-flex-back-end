package com.binarybrothers.gymflexapi.services.blogs;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.binarybrothers.gymflexapi.dtos.blogs.CategoryBlogDTO;
import com.binarybrothers.gymflexapi.entities.CategoryBlog;
import com.binarybrothers.gymflexapi.repositories.CategoryBlogRepository;
import com.binarybrothers.gymflexapi.services.mappers.CategoryBlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CategoryBlogServiceImpl implements CategoryBlogService {

    private final Logger log = LoggerFactory.getLogger(CategoryBlogServiceImpl.class);

    private final CategoryBlogRepository categoryBlogRepository;

    private final CategoryBlogMapper categoryBlogMapper;

    public CategoryBlogServiceImpl(CategoryBlogRepository categoryBlogRepository, CategoryBlogMapper categoryBlogMapper) {
        this.categoryBlogRepository = categoryBlogRepository;
        this.categoryBlogMapper = categoryBlogMapper;
    }

    @Override
    public CategoryBlogDTO save(CategoryBlogDTO categoryBlogDTO) {
        log.debug("Request to save CategoryBlog : {}", categoryBlogDTO);
        CategoryBlog categoryBlog = categoryBlogMapper.toEntity(categoryBlogDTO);
        categoryBlog = categoryBlogRepository.save(categoryBlog);
        return categoryBlogMapper.toDto(categoryBlog);
    }

    @Override
    public CategoryBlogDTO update(CategoryBlogDTO categoryBlogDTO) {
        log.debug("Request to update CategoryBlog : {}", categoryBlogDTO);
        CategoryBlog categoryBlog = categoryBlogMapper.toEntity(categoryBlogDTO);
        categoryBlog = categoryBlogRepository.save(categoryBlog);
        return categoryBlogMapper.toDto(categoryBlog);
    }

    @Override
    public Optional<CategoryBlogDTO> partialUpdate(CategoryBlogDTO categoryBlogDTO) {
        log.debug("Request to partially update CategoryBlog : {}", categoryBlogDTO);

        return categoryBlogRepository
            .findById(categoryBlogDTO.getId())
            .map(existingCategoryBlog -> {
                categoryBlogMapper.partialUpdate(existingCategoryBlog, categoryBlogDTO);

                return existingCategoryBlog;
            })
            .map(categoryBlogRepository::save)
            .map(categoryBlogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryBlogDTO> findAll() {
        log.debug("Request to get all CategoryBlogs");
        return categoryBlogRepository.findAll().stream().map(categoryBlogMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryBlogDTO> findOne(Long id) {
        log.debug("Request to get CategoryBlog : {}", id);
        return categoryBlogRepository.findById(id).map(categoryBlogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoryBlog : {}", id);
        categoryBlogRepository.deleteById(id);
    }
}

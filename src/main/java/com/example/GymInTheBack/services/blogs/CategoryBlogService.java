package com.example.GymInTheBack.services.blogs;

import com.example.GymInTheBack.dtos.blogs.CategoryBlogDTO;

import java.util.List;
import java.util.Optional;


public interface CategoryBlogService {
    /**
     * Save a categoryBlog.
     *
     * @param categoryBlogDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryBlogDTO save(CategoryBlogDTO categoryBlogDTO);

    /**
     * Updates a categoryBlog.
     *
     * @param categoryBlogDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryBlogDTO update(CategoryBlogDTO categoryBlogDTO);

    /**
     * Partially updates a categoryBlog.
     *
     * @param categoryBlogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryBlogDTO> partialUpdate(CategoryBlogDTO categoryBlogDTO);

    /**
     * Get all the categoryBlogs.
     *
     * @return the list of entities.
     */
    List<CategoryBlogDTO> findAll();

    /**
     * Get the "id" categoryBlog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryBlogDTO> findOne(Long id);

    /**
     * Delete the "id" categoryBlog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.CategoryBlog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoryBlog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryBlogRepository extends JpaRepository<CategoryBlog, Long> {}

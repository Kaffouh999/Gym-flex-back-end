package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.CategoryBlog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoryBlog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryBlogRepository extends JpaRepository<CategoryBlog, Long> {}

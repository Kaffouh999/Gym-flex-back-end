package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.SubCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {}

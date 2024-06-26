package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.SubCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findByName(String name);
}

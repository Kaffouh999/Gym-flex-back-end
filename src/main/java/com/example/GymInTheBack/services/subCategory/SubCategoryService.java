package com.example.GymInTheBack.services.subCategory;

import com.example.GymInTheBack.dtos.subCategory.SubCategoryDTO;
import java.util.List;
import java.util.Optional;


public interface SubCategoryService {
    /**
     * Save a subCategory.
     *
     * @param subCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    SubCategoryDTO save(SubCategoryDTO subCategoryDTO);

    /**
     * Updates a subCategory.
     *
     * @param subCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    SubCategoryDTO update(SubCategoryDTO subCategoryDTO);

    /**
     * Partially updates a subCategory.
     *
     * @param subCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubCategoryDTO> partialUpdate(SubCategoryDTO subCategoryDTO);

    /**
     * Get all the subCategories.
     *
     * @return the list of entities.
     */
    List<SubCategoryDTO> findAll();

    /**
     * Get the "id" subCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" subCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean existsByName(String name);
}

package com.binarybrothers.gymflexapi.services.mappers;


import com.binarybrothers.gymflexapi.dtos.product.ProductDTO;
import com.binarybrothers.gymflexapi.dtos.subcategory.SubCategoryDTO;
import com.binarybrothers.gymflexapi.entities.Product;
import com.binarybrothers.gymflexapi.entities.SubCategory;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "subCategoryId")
    ProductDTO toDto(Product s);

    @Named("subCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", source = "category")
    SubCategoryDTO toDtoSubCategoryId(SubCategory subCategory);
}

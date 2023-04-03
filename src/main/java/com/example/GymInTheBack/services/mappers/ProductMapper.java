package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.product.ProductDTO;
import com.example.GymInTheBack.dtos.subCategory.SubCategoryDTO;
import com.example.GymInTheBack.entities.Product;
import com.example.GymInTheBack.entities.SubCategory;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "subCategoryId")
    ProductDTO toDto(Product s);

    @Named("subCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", source = "category")
    SubCategoryDTO toDtoSubCategoryId(SubCategory subCategory);
}

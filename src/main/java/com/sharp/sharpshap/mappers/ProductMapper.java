package com.sharp.sharpshap.mappers;


import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductCreateDTO toDto(Product product);

    Product toEntity(ProductCreateDTO dto);


}

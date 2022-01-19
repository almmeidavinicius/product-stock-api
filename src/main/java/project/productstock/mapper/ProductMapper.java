package project.productstock.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.productstock.dto.ProductDTO;
import project.productstock.entity.Product;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toModel(ProductDTO productDTO);

    ProductDTO toDTO(Product product);
}



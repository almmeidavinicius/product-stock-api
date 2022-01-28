package project.productstock.service;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.productstock.builder.ProductDTOBuilder;
import project.productstock.dto.ProductDTO;
import project.productstock.entity.Product;
import project.productstock.exception.ProductAlreadyExistsException;
import project.productstock.exception.ProductNotFoundException;
import project.productstock.mapper.ProductMapper;
import project.productstock.repository.ProductRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper = ProductMapper.INSTANCE;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenProductInformedThenItShouldBeCreated() throws ProductAlreadyExistsException {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product product = productMapper.toModel(productDTO);

        // when
        Mockito.when(productRepository.findByCode(productDTO.getCode())).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ProductDTO createdProductDTO = productService.createProduct(productDTO);

        //then
        MatcherAssert.assertThat(createdProductDTO.getId(), Matchers.is(Matchers.equalTo(product.getId())));
        MatcherAssert.assertThat(createdProductDTO.getCode(), Matchers.is(Matchers.equalTo(product.getCode())));
        MatcherAssert.assertThat(createdProductDTO.getQuantity(), Matchers.is(Matchers.equalTo(product.getQuantity())));

    }

    @Test
    void whenAlreadyRegisteredProductInformedThenAnExceptionShouldBeThrown() throws ProductAlreadyExistsException {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product product = productMapper.toModel(productDTO);

        // when
        Mockito.when(productRepository.findByCode(productDTO.getCode())).thenReturn(Optional.of(product));

        // then
        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(productDTO));
    }

    @Test
    void whenValidProductCodeIsGivenThenReturnAProduct() throws ProductNotFoundException {
        // given
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);

        // when
        Mockito.when(productRepository.findByCode(expectedFoundProduct.getCode())).thenReturn(Optional.of(expectedFoundProduct));

        ProductDTO foundProductDTO = productService.findByCode(expectedFoundProductDTO.getCode());

        // then
        MatcherAssert.assertThat(foundProductDTO, Matchers.is(Matchers.equalTo(expectedFoundProductDTO)));
    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        // given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productRepository.findByCode(expectedProductDTO.getCode())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.findByCode(expectedProductDTO.getCode()));
    }
}

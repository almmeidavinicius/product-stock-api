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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.productstock.builder.ProductDTOBuilder;
import project.productstock.dto.ProductDTO;
import project.productstock.entity.Product;
import project.productstock.exception.ProductAlreadyExistsException;
import project.productstock.exception.ProductNotFoundException;
import project.productstock.mapper.ProductMapper;
import project.productstock.repository.ProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    /*@Test
    void whenValidProductCodeIsGivenThenReturnAProduct() throws ProductNotFoundException {
        // given
        ProductDTO expectedFoundProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedFoundProduct = productMapper.toModel(expectedFoundProductDTO);

        // when
        Mockito.when(productRepository.findByCode(expectedFoundProduct.getCode())).thenReturn(Optional.of(expectedFoundProduct));

        ProductDTO foundProductDTO = productService.getByCode(expectedFoundProductDTO.getCode());

        // then
        MatcherAssert.assertThat(foundProductDTO, Matchers.is(Matchers.equalTo(expectedFoundProductDTO)));
    }*/

    @Test
    void whenNotRegisteredProductNameIsGivenThenThrowAnException() {
        // given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productRepository.findByCode(expectedProductDTO.getCode())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getByCode(expectedProductDTO.getCode()));
    }

    /*@Test
    void whenListProductIsCalledThenReturnAListOfProducts() {
        // given
        ProductDTO expectedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedProduct = productMapper.toModel(expectedProductDTO);

        // when
        Mockito.when(productRepository.findAll()).thenReturn(Collections.singletonList(expectedProduct));

        List<ProductDTO> foundListProductDTO = productService.listAll();
        System.out.println(foundListProductDTO);

        // then
        MatcherAssert.assertThat(foundListProductDTO, Matchers.is(Matchers.not(Matchers.empty())));
        MatcherAssert.assertThat(foundListProductDTO.get(0), Matchers.is(Matchers.equalTo(expectedProductDTO)));
    }*/

    @Test
    void whenListProductIsCalledThenReturnAEmptyListOfProducts() {
        // when
        Mockito.when(productRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<ProductDTO> foundListProductDTO = productService.listAll();

        // then
        MatcherAssert.assertThat(foundListProductDTO, Matchers.is(Matchers.empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAProductShouldBeDeleted() throws ProductNotFoundException {
        // given
        ProductDTO expectedDeletedProductDTO = ProductDTOBuilder.builder().build().toProductDTO();
        Product expectedDeletedProduct = productMapper.toModel(expectedDeletedProductDTO);

        // when
        Mockito.when(productRepository.findById(expectedDeletedProductDTO.getId())).thenReturn(Optional.of(expectedDeletedProduct));
        Mockito.doNothing().when(productRepository).deleteById(expectedDeletedProductDTO.getId());

        // then
        productService.deleteById(expectedDeletedProductDTO.getId());

        Mockito.verify(productRepository, Mockito.times(1)).findById(expectedDeletedProductDTO.getId());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(expectedDeletedProductDTO.getId());
    }
}

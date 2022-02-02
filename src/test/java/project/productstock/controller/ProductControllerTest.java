package project.productstock.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import project.productstock.builder.ProductDTOBuilder;
import project.productstock.dto.ProductDTO;
import project.productstock.exception.ProductNotFoundException;
import project.productstock.service.ProductService;

import java.util.Collections;

import static org.hamcrest.core.Is.is;

import static project.productstock.utils.JsonConvertionUtils.asJsonString;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String PRODUCT_API_URL_PATH = "/api/v1/products";
    private static final long VALID_PRODUCT_ID = 1L;
    private static final long INVALID_PRODUCT_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAProductIsCreated() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productService.createProduct(productDTO)).thenReturn(productDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is(productDTO.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(productDTO.getDescription())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();
        productDTO.setMax(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productService.getByCode(productDTO.getCode())).thenReturn(productDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(productDTO.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(productDTO.getDescription())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productService.getByCode(productDTO.getCode())).thenThrow(ProductNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH + "/" + productDTO.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenGETListWithProductsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.when(productService.listAll()).thenReturn(Collections.singletonList(productDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code", is(productDTO.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(productDTO.getDescription())));
    }

    @Test
    void whenGETListWithoutProductsIsCalledThenOkStatusIsReturned() throws Exception {
        // when
        Mockito.when(productService.listAll()).thenReturn(Collections.EMPTY_LIST);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenDeleteIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        ProductDTO productDTO = ProductDTOBuilder.builder().build().toProductDTO();

        // when
        Mockito.doNothing().when(productService).deleteById(productDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/" + productDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        Mockito.doThrow(ProductNotFoundException.class).when(productService).deleteById(INVALID_PRODUCT_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(PRODUCT_API_URL_PATH + "/" + INVALID_PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

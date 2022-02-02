package project.productstock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.productstock.dto.ProductDTO;
import project.productstock.exception.ProductAlreadyExistsException;
import project.productstock.exception.ProductNotFoundException;
import project.productstock.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO productDTO) throws ProductAlreadyExistsException {
        return productService.createProduct(productDTO);
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO findByCode(@PathVariable String code) throws ProductNotFoundException {
        return productService.getByCode(code);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> listAll() {
        return productService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ProductNotFoundException {
        productService.deleteById(id);
    }
}

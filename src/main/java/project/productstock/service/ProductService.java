package project.productstock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.productstock.dto.ProductDTO;
import project.productstock.entity.Product;
import project.productstock.exception.ProductAlreadyExistsException;
import project.productstock.exception.ProductNotFoundException;
import project.productstock.mapper.ProductMapper;
import project.productstock.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public ProductDTO createProduct(ProductDTO productDTO) throws ProductAlreadyExistsException {
        checksIfProductAlreadyExists(productDTO.getCode());
        Product product = productMapper.toModel(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public ProductDTO getByCode(String code) throws ProductNotFoundException {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException(code));
        return productMapper.toDTO(product);
    }

    public List<ProductDTO> listAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws ProductNotFoundException {
        getById(id);
        productRepository.deleteById(id);
    }

    private void checksIfProductAlreadyExists(String code) throws ProductAlreadyExistsException {
        Optional<Product> optionalProduct = productRepository.findByCode(code);
        if (optionalProduct.isPresent()) {
            throw new ProductAlreadyExistsException(code);
        }
    }

    private Product getById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}


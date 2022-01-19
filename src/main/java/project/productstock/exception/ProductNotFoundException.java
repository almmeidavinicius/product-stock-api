package project.productstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String code) {
        super(String.format("Product with code %s not found", code));
    }

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %d not found", id));
    }
}

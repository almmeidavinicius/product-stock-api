package project.productstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductAlreadyExistsException extends Exception {
    public ProductAlreadyExistsException(String code) {
        super(String.format("Product with code %s already exists in the system.", code));
    }
}

package project.productstock.builder;

import lombok.Builder;
import project.productstock.dto.ProductDTO;

@Builder
public class ProductDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String code = "RWS0001";

    @Builder.Default
    private String description = "Mouse";

    @Builder.Default
    private Integer max = 50;

    @Builder.Default
    private Integer quantity = 10;

    public ProductDTO toProductDTO() {
        return new ProductDTO(
                id,
                code,
                description,
                max,
                quantity
        );
    }
}

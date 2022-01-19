package project.productstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @NotNull
    @Size(min = 7, max = 7)
    private String code;

    @NotNull
    private String description;

    @NotNull
    @Max(500)
    private Integer max;

    @NotNull
    @Max(500)
    private Integer quantity;
}

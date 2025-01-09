package mg.sandratra.bakery.dto.breadmaking;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.dto.product.ProductDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadmakingDto {
        private Long id;
    private ProductDto product;
    private Long createdBy;
    private Long quantity;
    private Date productionDate;
    private BigDecimal ingredientCost;
    private BigDecimal otherCost;
    private BigDecimal costPerUnit;
}

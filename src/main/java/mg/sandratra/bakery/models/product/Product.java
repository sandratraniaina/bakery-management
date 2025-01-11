package mg.sandratra.bakery.models.product;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.ProductType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private Long recipeId;
    private String name;
    private ProductType productType;
    private BigDecimal price;
    private Long stockQuantity;
    private Timestamp  createdAt;
}

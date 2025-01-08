package mg.sandratra.bakery.models.product;

import java.math.BigDecimal;
import java.security.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private Long recipeId;
    private String name;
    private BigDecimal price;
    private Long stockQuantity;
    private Timestamp  createdAt;
}

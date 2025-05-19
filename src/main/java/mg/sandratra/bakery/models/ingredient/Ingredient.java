package mg.sandratra.bakery.models.ingredient;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.IngredientType;
import mg.sandratra.bakery.enums.Unit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private Long id;
    private String name;
    private IngredientType ingredientType;
    private Unit unit;
    private BigDecimal costPerUnit;
    private BigDecimal stockQuantity;
    private BigDecimal minimumStock;
    private Timestamp lastUpdated;
}

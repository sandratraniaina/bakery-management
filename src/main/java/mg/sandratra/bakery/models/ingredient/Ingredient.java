package mg.sandratra.bakery.models.ingredient;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.Unit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private int id;
    private String name;
    private Unit unit;
    private double costPerUnit;
    private double stockQuantity;
    private double minimumStock;
    private Timestamp lastUpdated;
}

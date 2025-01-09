package mg.sandratra.bakery.models.breadmaking;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Breadmaking {
    private Long id;
    private Long productId;
    private Long createdBy;
    private Long quantity;
    private Date productionDate;
    private BigDecimal ingredientCost;
    private BigDecimal otherCost;
    private BigDecimal costPerUnit;
}

package mg.sandratra.bakery.services.breadmaking.filter;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreadkmakingFilter implements Filter {
    Long ingredientId;

    @Override
    public String buildQuery() {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM v_breadmaking_ingredients WHERE 1 = 1");

        if (ingredientId != null) { 
            queryBuilder.append(" AND ingredient_id = :ingredientId");
        }

        return queryBuilder.toString();
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();

        if (ingredientId != null) {
            params.put("ingredientId", ingredientId);
        }

        return params;
    }
}

package mg.sandratra.bakery.services.sale.filter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.ProductType;
import mg.sandratra.bakery.utils.filter.BooleanValue;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleFilter implements Filter {
    private Boolean isNature;
    private ProductType productType;

    public Boolean getIsNature() {
        return isNature;
    }

    public static List<BooleanValue> getBooleanValues() {
        return List.of(
                new BooleanValue("Nature", true),
                new BooleanValue("Non-nature", false));
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT DISTINCT s.* FROM sale s ")
                .append("JOIN (")
                .append("    SELECT DISTINCT spn.id ")
                .append("    FROM v_sale_product_nature spn ")
                .append("    WHERE 1=1 ");

        if (productType != null) {
            query.append("    AND spn.product_type = CAST(:productType AS product_type) ");
        }

        if (isNature != null) {
            query.append("    AND spn.is_nature = :isNature ");
        }

        query.append(") filtered_sales ON s.id = filtered_sales.id ")
                .append("ORDER BY s.sale_date DESC, s.id");

        return query.toString();
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();

        if (productType != null) {
            params.put("productType", productType.name());
        }

        if (isNature != null) {
            params.put("isNature", isNature);
        }

        return params;
    }
}

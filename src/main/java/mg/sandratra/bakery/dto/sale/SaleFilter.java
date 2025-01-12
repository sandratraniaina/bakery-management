package mg.sandratra.bakery.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.enums.ProductType;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleFilter implements Filter {
    private boolean isNature;
    private ProductType productType;

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM v_breadmaking_product_nature bmpn ");

        if (productType != null || isNature) {
            query.append("WHERE 1=1 ");

            if (productType != null) {
                query.append("AND bmpn.product_type = '")
                        .append(productType.name())
                        .append("' ");
            }

            if (isNature) {
                query.append("AND bmpn.is_nature = true ");
            }
        }

        query.append("ORDER BY bmpn.production_date, bmpn.id");

        return query.toString();
    }
}

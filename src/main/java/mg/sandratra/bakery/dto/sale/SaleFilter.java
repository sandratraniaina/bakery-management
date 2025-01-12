package mg.sandratra.bakery.dto.sale;

import java.util.List;

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
            new BooleanValue("Non-nature", false)
        );
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM v_sale_product_nature spn ");

        if (productType != null || isNature != null) {
            query.append("WHERE 1=1 ");

            if (productType != null) {
                query.append("AND spn.product_type = '")
                        .append(productType.name())
                        .append("' ");
            }

            if (isNature != null) {
                query.append("AND spn.is_nature = ");
                query.append(isNature + " ");
            }
        }

        System.out.println(query.toString());

        return query.toString();
    }
}

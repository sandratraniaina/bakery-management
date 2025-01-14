package mg.sandratra.bakery.dto.product;

import java.sql.Date;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.utils.DateUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsDto {
    private Long id;
    private Date createdAt;
    private Product product;

    public String getMonth() {
        return DateUtils.getMonthInText(createdAt, Locale.ENGLISH);
    }

    public int getYear() {
        return createdAt.toLocalDate().getYear();
    }

    public String getProductName() {
        return product.getName();
    }
}

package mg.sandratra.bakery.dto.sale;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {
    private Long id;
    private Long createdBy;
    private String clientName;
    private List<SaleDetailsDto> saleDetails;
    private BigDecimal totalAmount;
    private Date saleDate;
    private Timestamp createdAt;

    /**
     * Returns a string with the product names and their types in the format:
     * "productName(productType)" separated by commas.
     * Example: "bread(BREAD), croissant(VIENNOISERIE)"
     */
    public String getProductNamesWithTypes() {
        if (saleDetails == null || saleDetails.isEmpty()) {
            return "";
        }

        return saleDetails.stream()
                .map(detail -> {
                    Product product = detail.getProduct();
                    if (product != null) {
                        String productName = product.getName();
                        String productType = product.getProductType() != null
                                ? product.getProductType().getValue()
                                : "UNKNOWN";
                        return productName + "(" + productType + ")";
                    }
                    return "UNKNOWN_PRODUCT";
                })
                .collect(Collectors.joining(", "));
    }
}

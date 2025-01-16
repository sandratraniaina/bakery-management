package mg.sandratra.bakery.models.sale;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    private Long id;
    private Long createdBy;
    private String clientName = "Anonymous";
    private BigDecimal totalAmount;
    private Date saleDate;
    private Timestamp createdAt;
}

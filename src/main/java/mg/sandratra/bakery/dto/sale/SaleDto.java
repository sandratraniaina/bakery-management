package mg.sandratra.bakery.dto.sale;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

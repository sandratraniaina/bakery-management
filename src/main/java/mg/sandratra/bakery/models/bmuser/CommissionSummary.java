package mg.sandratra.bakery.models.bmuser;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionSummary {
    Gender gender;
    BigDecimal totalCommission;
}

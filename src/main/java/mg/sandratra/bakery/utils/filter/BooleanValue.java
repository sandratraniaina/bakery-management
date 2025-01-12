package mg.sandratra.bakery.utils.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooleanValue {
    private String text;
    private Boolean value;
}

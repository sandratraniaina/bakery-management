package mg.sandratra.bakery.models.recipe;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private Long id;
    private String name;
    private String description;
    private Timestamp createdAt;
}

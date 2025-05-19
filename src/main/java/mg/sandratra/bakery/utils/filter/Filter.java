package mg.sandratra.bakery.utils.filter;

import java.util.Map;

public interface Filter {
    public abstract String buildQuery();
    public Map<String, Object> getParameters();
}

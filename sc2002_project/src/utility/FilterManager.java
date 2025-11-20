package utility;

import java.util.HashMap;
import java.util.Map;

public class FilterManager {
    private final Map<String, UserFilterSettings> userFilters = new HashMap<>();

    public UserFilterSettings getFilters(String userId) {
        return userFilters.get(userId);
    }

    public void setFilters(String userId, UserFilterSettings settings) {
        userFilters.put(userId, settings);
    }

    public void clearFilters(String userId) {
        userFilters.remove(userId);
    }
}

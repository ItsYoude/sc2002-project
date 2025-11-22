package utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages user-specific filter settings in memory.
 * 
 * <p>
 * Each user can have their own {@link UserFilterSettings} which can be retrieved,
 * updated, or cleared. This class acts as a simple in-memory storage for user filters.
 * </p>
 */

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

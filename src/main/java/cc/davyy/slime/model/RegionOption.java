package cc.davyy.slime.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents options for a region, such as block break protection.
 */
public class RegionOption {

    private final Map<String, Boolean> options = new HashMap<>();

    /**
     * Sets an option for the region.
     *
     * @param key The option key (e.g., "blockBreak").
     * @param enabled True to enable the option, false to disable it.
     */
    public void setOption(@NotNull String key, boolean enabled) { options.put(key, enabled); }

    /**
     * Checks if an option is enabled for the region.
     *
     * @param key The option key (e.g., "blockBreak").
     * @return True if the option is enabled, false otherwise.
     */
    public boolean isOptionEnabled(@NotNull String key) { return options.getOrDefault(key, false); }

    /**
     * Removes an option from the region.
     *
     * @param key The option key to remove.
     */
    public void removeOption(@NotNull String key) { options.remove(key); }

    /**
     * Checks if the region has a specific option set.
     *
     * @param key The option key to check.
     * @return True if the option exists, false otherwise.
     */
    public boolean hasOption(@NotNull String key) { return options.containsKey(key); }

}
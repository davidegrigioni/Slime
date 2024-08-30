package cc.davyy.slime.managers;

import cc.davyy.slime.model.Region;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.util.*;

@Singleton
public class RegionManager {

    private final Map<UUID, Vec> point1Map = new HashMap<>();
    private final Map<UUID, Vec> point2Map = new HashMap<>();
    private final Map<UUID, String> regionSetupMap = new HashMap<>();
    private final Map<String, Region> regions = new HashMap<>();

    /**
     * Starts the setup process for a region.
     *
     * @param playerUuid The UUID of the player.
     * @param regionName The name of the region.
     */
    public void startRegionSetup(UUID playerUuid, String regionName) {
        regionSetupMap.put(playerUuid, regionName);
        point1Map.remove(playerUuid);
        point2Map.remove(playerUuid);
    }

    /**
     * Checks if a player is in the process of setting up a region.
     *
     * @param playerUuid The UUID of the player.
     * @return True if the player is setting up a region, false otherwise.
     */
    public boolean isSettingUpRegion(UUID playerUuid) {
        return regionSetupMap.containsKey(playerUuid);
    }

    /**
     * Sets the first point for the region setup.
     *
     * @param playerUuid The UUID of the player.
     * @param point      The position to set as the first point.
     */
    public void setPoint1(UUID playerUuid, Vec point) {
        point1Map.put(playerUuid, point);
    }

    /**
     * Sets the second point for the region setup.
     *
     * @param playerUuid The UUID of the player.
     * @param point      The position to set as the second point.
     */
    public void setPoint2(UUID playerUuid, Vec point) {
        point2Map.put(playerUuid, point);
    }

    /**
     * Saves the region being set up.
     *
     * @param playerUuid The UUID of the player.
     * @return The created region, or null if the region could not be created.
     */
    public Region saveRegion(UUID playerUuid) {
        String regionName = regionSetupMap.get(playerUuid);
        Vec point1 = point1Map.get(playerUuid);
        Vec point2 = point2Map.get(playerUuid);

        if (regionName != null && point1 != null && point2 != null) {
            Region region = new Region(point1, point2);
            regions.put(regionName, region);
            regionSetupMap.remove(playerUuid);
            return region;
        }

        return null;
    }

    /**
     * Retrieves a region by its name.
     *
     * @param regionName The name of the region.
     * @return The region, or null if it doesn't exist.
     */
    public Region getRegion(String regionName) {
        return regions.get(regionName);
    }

    /**
     * Checks if a player is inside any region.
     *
     * @param playerPos The current position of the player.
     * @return The name of the region the player is in, or null if the player is not in any region.
     */
    public String isPlayerInRegion(Pos playerPos) {
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            Region region = entry.getValue();
            if (region.contains(playerPos)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Cancels the region setup process for a player.
     *
     * @param playerUuid The UUID of the player.
     */
    public void cancelRegionSetup(UUID playerUuid) {
        regionSetupMap.remove(playerUuid);
        point1Map.remove(playerUuid);
        point2Map.remove(playerUuid);
    }

}
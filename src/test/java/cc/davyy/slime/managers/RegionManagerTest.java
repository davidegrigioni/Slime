package cc.davyy.slime.managers;

import cc.davyy.slime.model.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegionManagerTest {

    private RegionManager regionManager;
    private UUID playerUuid;

    @BeforeEach
    void setUp() {
        regionManager = new RegionManager();
        playerUuid = UUID.randomUUID();
    }

    @Test
    void testStartRegionSetup() {
        String regionName = "TestRegion";
        regionManager.startRegionSetup(playerUuid, regionName);

        assertTrue(regionManager.isSettingUpRegion(playerUuid));
    }

    @Test
    void testSetPointsAndSaveRegion() {
        String regionName = "TestRegion";
        regionManager.startRegionSetup(playerUuid, regionName);

        Vec point1 = new Vec(0, 0, 0);
        Vec point2 = new Vec(10, 10, 10);
        regionManager.setPoint1(playerUuid, point1);
        regionManager.setPoint2(playerUuid, point2);

        Region region = regionManager.saveRegion(playerUuid);
        assertNotNull(region);
        assertEquals(point1, region.getMinPoint());
        assertEquals(point2, region.getMaxPoint());

        Region retrievedRegion = regionManager.getRegion(regionName);
        assertEquals(region, retrievedRegion);
    }

    @Test
    void testPlayerInRegion() {
        String regionName = "TestRegion";
        regionManager.startRegionSetup(playerUuid, regionName);

        Vec point1 = new Vec(0, 0, 0);
        Vec point2 = new Vec(10, 10, 10);
        regionManager.setPoint1(playerUuid, point1);
        regionManager.setPoint2(playerUuid, point2);
        regionManager.saveRegion(playerUuid);

        Pos insidePos = new Pos(5, 5, 5);
        Pos outsidePos = new Pos(15, 15, 15);

        assertEquals(regionName, regionManager.isPlayerInRegion(insidePos));
        assertNull(regionManager.isPlayerInRegion(outsidePos));
    }

    @Test
    void testCancelRegionSetup() {
        String regionName = "TestRegion";
        regionManager.startRegionSetup(playerUuid, regionName);

        regionManager.cancelRegionSetup(playerUuid);

        assertFalse(regionManager.isSettingUpRegion(playerUuid));
    }

    @Test
    void testSaveRegionWithoutPoints() {
        String regionName = "TestRegion";
        regionManager.startRegionSetup(playerUuid, regionName);

        Region region = regionManager.saveRegion(playerUuid);

        assertNull(region);
    }

}
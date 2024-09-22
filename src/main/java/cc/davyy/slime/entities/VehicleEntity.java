package cc.davyy.slime.entities;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Vehicle;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class VehicleEntity extends EntityCreature {

    private SlimePlayer driver;
    private final Vehicle vehicleData;

    public VehicleEntity(@NotNull Vehicle vehicle, @NotNull Instance instance, @NotNull Pos spawn) {
        super(vehicle.entityType());
        this.vehicleData = vehicle;

        setBoundingBox(1.5f, 1.0f, 1.5f);
        setNoGravity(true);
        setTag(TagConstants.VEHICLE_ID_TAG, vehicle.id());

        setInstance(instance, spawn);
    }

    /**
     * Call this method to assign a driver (player) to the vehicle.
     * This could be called when a player interacts with the vehicle.
     */
    public void setDriver(@NotNull SlimePlayer player) {
        this.driver = player;
        player.addPassenger(this);
    }

    /**
     * Remove the driver from the vehicle.
     */
    public void removeDriver() {
        if (this.driver != null) {
            this.getVehicle().removePassenger(driver);
            this.driver = null;
        }
    }

    /**
     * Cleanup when the vehicle is destroyed or removed.
     */
    @Override
    public void remove() {
        removeDriver();
        super.remove();
    }

    /**
     * Get the speed of the vehicle.
     */
    public double getVehicleSpeed() {
        return vehicleData.vehicleSpeed();
    }

    /**
     * Get the UUID of the player who owns the vehicle.
     */
    public UUID getPlayerUUID() {
        return vehicleData.playerUUID();
    }

}
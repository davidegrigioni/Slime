package cc.davyy.slime.entities;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import org.jetbrains.annotations.NotNull;

public final class VehicleEntity extends EntityCreature {

    private SlimePlayer driver;

    public VehicleEntity(@NotNull EntityType entityType, @NotNull String vehicleName, @NotNull Instance instance, @NotNull Pos spawn) {
        super(entityType);

        setBoundingBox(1.5f, 1.0f, 1.5f);
        setNoGravity(true);
        setTag(TagConstants.VEHICLE_NAME_TAG, vehicleName);

        setInstance(instance, spawn);
    }

    public void handleSteerPacket(ClientSteerVehiclePacket packet) {
        final float fwSpeed = packet.forward();
        final float swSpeed = packet.sideways();
        final byte flags = packet.flags();

        if (driver != null) {
            final Pos pos = driver.getPosition();

            setView(pos.yaw(), pos.pitch());

            final Vec forwardDir = pos.direction();
            final Vec sideways = forwardDir.cross(new Vec(0, -1, 0));
            final Vec total = forwardDir.mul(fwSpeed).add(sideways.mul(swSpeed));

            setVelocity(getVelocity().add(total));

            if (flags == 2) {
                removePassenger(driver);
            }
        }
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

}
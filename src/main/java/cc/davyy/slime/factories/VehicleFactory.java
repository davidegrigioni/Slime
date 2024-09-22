package cc.davyy.slime.factories;

import cc.davyy.slime.entities.VehicleEntity;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Vehicle;
import com.google.inject.Singleton;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Singleton
public class VehicleFactory {

    public VehicleEntity createVehicle(int id, @NotNull EntityType entityType,
                                       double initialSpeed,
                                       @NotNull SlimePlayer player) {
        final Vehicle vehicle = new Vehicle(id, entityType, initialSpeed, player.getUuid());
        return new VehicleEntity(vehicle, player.getInstance(), player.getPosition());
    }

}
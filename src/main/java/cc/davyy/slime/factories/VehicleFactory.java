package cc.davyy.slime.factories;

import cc.davyy.slime.entities.VehicleEntity;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class VehicleFactory {

    public VehicleEntity createVehicle(@NotNull EntityType entityType,
                                       @NotNull String vehicleName,
                                       @NotNull SlimePlayer player) {
        return new VehicleEntity(entityType, vehicleName, player.getInstance(), player.getPosition());
    }

}
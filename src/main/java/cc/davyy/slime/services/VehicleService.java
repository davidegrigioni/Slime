package cc.davyy.slime.services;

import cc.davyy.slime.entities.VehicleEntity;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Vehicle;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface VehicleService {

    void createVehicle(@NotNull EntityType entityType,
                       double initialSpeed,
                       @NotNull SlimePlayer player);

    void removeVehicle(int id);

    Optional<VehicleEntity> getVehicleById(int id);

    List<VehicleEntity> getAllVehicles();

    void assignDriver(int vehicleId, @NotNull SlimePlayer player);

    void removeDriver(int vehicleId);

    void updateVehicleSpeed(int vehicleId, double newSpeed);

    boolean vehicleExists(int id);

    Optional<SlimePlayer> getDriver(int vehicleId);

    void teleportVehicle(int vehicleId, @NotNull Pos newPosition);

    List<VehicleEntity> getVehiclesByPlayer(@NotNull SlimePlayer player);

    void clearAllVehicles();

}
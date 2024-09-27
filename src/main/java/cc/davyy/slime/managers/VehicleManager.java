package cc.davyy.slime.managers;

import cc.davyy.slime.entities.VehicleEntity;
import cc.davyy.slime.factories.VehicleFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.VehicleService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class VehicleManager implements VehicleService {

    private static final AtomicInteger vehicleIDCounter = new AtomicInteger();

    private final VehicleFactory vehicleFactory;
    private final Int2ObjectMap<VehicleEntity> vehicles = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<SlimePlayer> vehicleDrivers = new Int2ObjectOpenHashMap<>();

    @Inject
    public VehicleManager(VehicleFactory vehicleFactory) {
        this.vehicleFactory = vehicleFactory;
    }

    @Override
    public void createVehicle(@NotNull EntityType entityType, double initialSpeed, @NotNull SlimePlayer player) {
        final int vehicleID = vehicleIDCounter.getAndIncrement();
        final VehicleEntity vehicle = vehicleFactory.createVehicle(vehicleID, entityType, initialSpeed, player);

        vehicles.put(vehicleID, vehicle);
    }

    @Override
    public void removeVehicle(int id) {
        vehicles.remove(id);
        vehicleDrivers.remove(id);
    }

    @Override
    public Optional<VehicleEntity> getVehicleById(int id) {
        return Optional.ofNullable(vehicles.get(id));
    }

    @Override
    public List<VehicleEntity> getAllVehicles() {
        return List.copyOf(vehicles.values());
    }

    @Override
    public void assignDriver(int vehicleId, @NotNull SlimePlayer player) {
        final VehicleEntity vehicle = vehicles.get(vehicleId);
        if (vehicles.containsKey(vehicleId)) {
            vehicleDrivers.put(vehicleId, player);
            vehicle.setDriver(player);
        }
    }

    @Override
    public void removeDriver(int vehicleId) {
        final VehicleEntity vehicle = vehicles.get(vehicleId);
        if (vehicles.containsKey(vehicleId)) {
            vehicle.removeDriver();
            vehicleDrivers.remove(vehicleId);
        }
    }

    @Override
    public void updateVehicleSpeed(int vehicleId, double newSpeed) {

    }

    @Override
    public boolean vehicleExists(int id) {
        return vehicles.containsKey(id);
    }

    @Override
    public Optional<SlimePlayer> getDriver(int vehicleId) {
        return Optional.ofNullable(vehicleDrivers.get(vehicleId));
    }

    @Override
    public void teleportVehicle(int vehicleId, @NotNull Pos newPosition) {

    }

    @Override
    public List<VehicleEntity> getVehiclesByPlayer(@NotNull SlimePlayer player) {
        return List.of();
    }

    @Override
    public void clearAllVehicles() {
        vehicles.clear();
        vehicleDrivers.clear();
    }

}
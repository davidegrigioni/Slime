package cc.davyy.slime.managers;

import cc.davyy.slime.factories.ParkourFactory;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.parkour.Parkour;
import cc.davyy.slime.model.parkour.ParkourCheckpoint;
import cc.davyy.slime.model.parkour.ParkourSession;
import cc.davyy.slime.services.ParkourService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ParkourManager implements ParkourService {

    private final ParkourFactory parkourFactory;
    private final Map<Integer, Parkour> parkours = new ConcurrentHashMap<>();
    private final Map<UUID, ParkourSession> activeSessions = new ConcurrentHashMap<>();
    private final AtomicInteger parkourIdGenerator = new AtomicInteger();

    @Inject
    public ParkourManager(ParkourFactory parkourFactory) {
        this.parkourFactory = parkourFactory;
    }

    @Override
    public void createParkour(@NotNull Component name) {
        int id = parkourIdGenerator.getAndIncrement();
        ParkourCheckpoint checkpoint = new ParkourCheckpoint(id, new CopyOnWriteArrayList<>(), false);
        Parkour parkour = parkourFactory.createParkour(id, name, checkpoint);
        parkours.put(id, parkour);
    }

    @Override
    public void deleteParkour(int id) {
        parkours.remove(id);
    }

    @Override
    public Optional<Parkour> getParkour(int id) {
        return Optional.ofNullable(parkours.get(id));
    }

    @Override
    public List<Parkour> getAllParkours() {
        return List.copyOf(parkours.values());
    }

    @Override
    public void startParkour(@NotNull SlimePlayer player, int parkourId) {
        Parkour parkour = parkours.get(parkourId);
        if (parkour != null) {
            ParkourSession session = new ParkourSession(parkour, System.currentTimeMillis(), 0);
            activeSessions.put(player.getUuid(), session);
            player.setParkourCourse(parkourId);
            player.setParkourStartTime(System.currentTimeMillis());
        }
    }

    @Override
    public void endParkour(@NotNull SlimePlayer player) {
        ParkourSession session = activeSessions.remove(player.getUuid());
        if (session != null) {
            long endTime = System.currentTimeMillis();
            long startTime = player.getParkourStartTime();
            long duration = endTime - startTime;

            player.setParkourCompletionStatus(true);
        }
    }

    @Override
    public void setCheckpoint(@NotNull SlimePlayer player) {
        ParkourSession session = activeSessions.get(player.getUuid());
        if (session != null) {
            Parkour parkour = session.parkour();
            ParkourCheckpoint checkpoint = parkour.checkpoint();
            checkpoint.checkpoints().add(new Pos(player.getPosition())); // Assuming Pos can be created from player position
            parkour = new Parkour(parkour.id(), parkour.name(), checkpoint);
            parkours.put(parkour.id(), parkour);

            player.setParkourCheckpoint(checkpoint.checkpoints().size());
        }
    }

    @Override
    public void editCheckpoint(@NotNull SlimePlayer player, int checkpointId) {
        // Implement editing logic based on checkpointId
    }

    @Override
    public void finishParkour(@NotNull SlimePlayer player) {
        ParkourSession session = activeSessions.get(player.getUuid());
        if (session != null) {
            Parkour parkour = session.parkour();
            ParkourCheckpoint checkpoint = parkour.checkpoint();
            if (checkpoint.isFinalCheckpoint() && !checkpoint.checkpoints().isEmpty()) {
                endParkour(player);
            } else {
                // Notify player that parkour is not completed correctly
            }
        }
    }

}
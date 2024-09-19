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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ParkourManager implements ParkourService {

    private final ParkourFactory parkourFactory;
    private final Map<UUID, ParkourSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<Integer, Parkour> parkourMap = new ConcurrentHashMap<>();
    private final AtomicInteger parkourID = new AtomicInteger();

    @Inject
    public ParkourManager(ParkourFactory parkourFactory) {
        this.parkourFactory = parkourFactory;
    }

    @Override
    public void createParkour(@NotNull Component name) {
        int id = parkourID.getAndIncrement();
    }

    @Override
    public void deleteParkour(int id) {
        parkourMap.remove(id);
    }

    @Override
    public Parkour getParkour(int id) {
        return parkourMap.get(id);
    }

    @Override
    public List<Parkour> getAllParkours() {
        return List.copyOf(parkourMap.values());
    }

    @Override
    public void startParkour(@NotNull SlimePlayer player, int parkourId) {
        final Parkour parkour = getParkour(parkourId);
        if (parkour != null) {
            final ParkourSession session = new ParkourSession(parkour, System.currentTimeMillis(), 0);
            activeSessions.put(player.getUuid(), session);
            player.sendMessage("Parkour Started");
        }
    }

    @Override
    public void endParkour(@NotNull SlimePlayer player) {
        final ParkourSession session = activeSessions.remove(player.getUuid());
        if (session != null) {
            long duration = System.currentTimeMillis() - session.startTime();
            player.sendMessage("Parkour ended" + duration);
        }
    }

    @Override
    public void updateCheckpoint(@NotNull SlimePlayer player) {
        final ParkourSession session = activeSessions.get(player.getUuid());
        if (session != null) {
            int nextCheckpoint = session.currentCheckpoint() + 1;

            if (nextCheckpoint < session.parkour().parkourCheckpoint().checkpointList().size()) {
                activeSessions.put(player.getUuid(),
                        new ParkourSession(session.parkour(), session.startTime(), nextCheckpoint));
                player.sendMessage("new checkpoint " + nextCheckpoint);
                return;
            }

            endParkour(player);
        }
    }

}
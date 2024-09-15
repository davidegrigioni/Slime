package cc.davyy.slime.managers;

import cc.davyy.slime.services.TeleportService;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TeleportManager implements TeleportService {

    private final Map<UUID, Long> teleportCooldowns = new ConcurrentHashMap<>();
    private static final long TELEPORT_COOLDOWN_MILLIS = 5000;

    @Override
    public void teleportPlayerToTarget(@NotNull SlimePlayer player, @NotNull SlimePlayer target) {
        long currentTime = System.currentTimeMillis();

        if (!player.hasPermission("teleport.bypass")) {
            Long lastTeleportTime = teleportCooldowns.get(player.getUuid());

            if (lastTeleportTime != null && (currentTime - lastTeleportTime) < TELEPORT_COOLDOWN_MILLIS) {
                long remainingTime = (TELEPORT_COOLDOWN_MILLIS - (currentTime - lastTeleportTime)) / 1000;
                player.sendMessage("You must wait " + remainingTime + " seconds before teleporting again.");
                return;
            }
        }

        player.teleport(target.getPosition()).thenRun(() -> {
            teleportCooldowns.put(player.getUuid(), currentTime);
            player.sendMessage("You have been teleported to " + target.getUsername() + "!");
        });
    }

}
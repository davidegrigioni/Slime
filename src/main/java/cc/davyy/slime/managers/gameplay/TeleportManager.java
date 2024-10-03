package cc.davyy.slime.managers.gameplay;

import cc.davyy.slime.services.gameplay.TeleportService;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.model.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TeleportManager implements TeleportService {

    private static final long TELEPORT_COOLDOWN_MILLIS = 5000;

    private final Map<UUID, Long> teleportCooldowns = new ConcurrentHashMap<>();

    @Override
    public void teleportPlayerToTarget(@NotNull SlimePlayer player, @NotNull SlimePlayer target) {
        if (player.equals(target)) {
            player.sendMessage(Messages.TELEPORT_YOURSELF_TO_YOURSELF_ERROR
                    .asComponent());
            return;
        }

        final long currentTime = System.currentTimeMillis();

        if (!player.hasPermission("slime.teleportbypass")) {
            final Long lastTeleportTime = teleportCooldowns.get(player.getUuid());

            if (lastTeleportTime != null && (currentTime - lastTeleportTime) < TELEPORT_COOLDOWN_MILLIS) {
                long remainingTime = (TELEPORT_COOLDOWN_MILLIS - (currentTime - lastTeleportTime)) / 1000;
                player.sendMessage(Messages.TELEPORT_WAIT_TIME
                        .addPlaceholder("time", String.valueOf(remainingTime))
                        .asComponent());
                return;
            }
        }

        final Pos targetPos = target.getPosition();
        final Instance targetInstance = target.getInstance();
        final Instance playerInstance = player.getInstance();

        if (!playerInstance.equals(targetInstance)) {
            player.setInstance(targetInstance).thenRun(() -> handleTeleport(player, targetPos, currentTime, target));
            return;
        }

        handleTeleport(player, targetPos, currentTime, target);
    }

    @Override
    public void teleportPlayerToCoordinates(@NotNull SlimePlayer player, @NotNull Pos position) {
        if (position.x() < -30000000 || position.x() > 30000000 ||
                position.y() < -64 || position.y() > 320 ||
                position.z() < -30000000 || position.z() > 30000000) {

            player.sendMessage(Messages.COORDINATES_OUT_OF_BOUND
                    .asComponent());
            return;
        }

        player.teleport(PosUtils.of(position));

        player.sendMessage(Messages.TELEPORT_TO_COORDINATE
                .addPlaceholder("pos", PosUtils.toString(position))
                .asComponent());
    }

    @Override
    public void teleportTargetToExecutor(@NotNull SlimePlayer executor, @NotNull SlimePlayer target) {
        if (executor.equals(target)) {
            executor.sendMessage(Messages.TELEPORT_YOURSELF_TO_YOURSELF_ERROR
                    .asComponent());
            return;
        }

        final Pos executorPosition = executor.getPosition();
        target.teleport(executorPosition);

        target.sendMessage(Messages.TELEPORT_TARGET_MESSAGE
                .addPlaceholder("player", executor.getUsername())
                .asComponent());

        executor.sendMessage(Messages.TELEPORT_EXECUTOR_MESSAGE
                .addPlaceholder("target", target.getUsername())
                .asComponent());
    }

    private void handleTeleport(@NotNull SlimePlayer player, @NotNull Pos targetPos, long currentTime, @NotNull SlimePlayer targetUsername) {
        player.teleport(targetPos).thenRun(() -> {
            teleportCooldowns.put(player.getUuid(), currentTime);
            player.sendMessage(Messages.TELEPORT_TO_PLAYER
                    .addPlaceholder("target", targetUsername.getUsername())
                    .asComponent());
        });
    }

}
package cc.davyy.slime.listeners;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.managers.SpawnManager;
import cc.davyy.slime.model.SlimePlayer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import static net.minestom.server.MinecraftServer.LOGGER;

@Singleton
public class PlayerMoveListener implements EventListener<PlayerMoveEvent> {

    private final SpawnManager spawnManager;

    @Inject
    public PlayerMoveListener(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @Override
    public @NotNull Class<PlayerMoveEvent> eventType() {
        return PlayerMoveEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerMoveEvent event) {
        final SlimePlayer player = (SlimePlayer) event.getPlayer();
        final Instance playerInstance = player.getInstance();

        final Integer deathY = playerInstance.getTag(TagConstants.DEATH_Y);

        if (deathY == null) {
            LOGGER.warn("Death Y value is missing for instance: {}", playerInstance.getUniqueId());
            return Result.INVALID;
        }

        if (player.getPosition().y() < deathY) {
            spawnManager.teleportToSpawn(player);
        }

        return Result.SUCCESS;
    }

}
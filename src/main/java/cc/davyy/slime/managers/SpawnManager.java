package cc.davyy.slime.managers;

import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cc.davyy.slime.utils.FileUtils.getConfig;

@Singleton
public class SpawnManager {

    private Pos spawnPosition;

    public SpawnManager() {
        final String posString = getConfig().getString("spawn.position");
        if (posString != null) {
            spawnPosition = PosUtils.fromString(posString);
        }
    }

    public void setSpawnPosition(@NotNull Pos pos) {
        spawnPosition = pos;
        getConfig().set("spawn.position", PosUtils.toString(pos));
    }

    @Nullable
    public Pos getSpawnPosition() {
        return spawnPosition;
    }

    public void teleportToSpawn(@NotNull Player player) {
        if (spawnPosition == null) {
            throw new IllegalStateException("Spawn position is not set.");
        }
        player.teleport(spawnPosition).thenRun(() ->
                player.sendMessage(Messages.SPAWN_TELEPORT.asComponent()));
    }

}
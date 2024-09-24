package cc.davyy.slime.managers;

import cc.davyy.slime.services.SpawnService;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.utils.FileUtils;
import cc.davyy.slime.utils.Messages;
import cc.davyy.slime.utils.PosUtils;
import com.google.inject.Singleton;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class SpawnManager implements SpawnService {

    private Pos spawnPosition;

    public SpawnManager() {
        final String posString = FileUtils.getConfig().getString("spawn.position");
        if (posString != null) {
            spawnPosition = PosUtils.fromString(posString);
        }
    }

    @Override
    public void setSpawnPosition(@NotNull Pos pos) {
        spawnPosition = pos;
        FileUtils.getConfig().getFileData().insert("spawn.position", PosUtils.toString(pos));
    }

    @Override
    public @Nullable Pos getSpawnPosition() {
        return spawnPosition;
    }

    @Override
    public void teleportToSpawn(@NotNull SlimePlayer player) {
        if (spawnPosition == null) {
            throw new IllegalStateException("Spawn position is not set.");
        }
        player.teleport(spawnPosition).thenRun(() ->
                player.sendMessage(Messages.SPAWN_TELEPORT.asComponent()));
    }

}
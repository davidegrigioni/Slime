package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SpawnService {

    void setSpawnPosition(@NotNull Pos pos);

    @Nullable Pos getSpawnPosition();

    void teleportToSpawn(@NotNull SlimePlayer player);

}
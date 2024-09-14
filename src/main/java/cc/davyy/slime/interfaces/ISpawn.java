package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISpawn {

    void setSpawnPosition(@NotNull Pos pos);

    @Nullable Pos getSpawnPosition();

    void teleportToSpawn(@NotNull SlimePlayer player);

}
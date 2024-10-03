package cc.davyy.slime.services.gameplay;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface VanishService {

    void vanish(@NotNull SlimePlayer player);

    void unvanish(@NotNull SlimePlayer player);

    boolean isPlayerVanished(@NotNull SlimePlayer player);

}
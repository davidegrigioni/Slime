package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface ITeleport {

    void teleportPlayerToTarget(@NotNull SlimePlayer player, @NotNull SlimePlayer target);

}
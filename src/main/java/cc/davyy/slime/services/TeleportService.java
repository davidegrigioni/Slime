package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface TeleportService {

    void teleportPlayerToTarget(@NotNull SlimePlayer player, @NotNull SlimePlayer target);

}
package cc.davyy.slime.services.entities;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public interface DisguiseService {

    void disguise(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull String nickName);

    void undisguise(@NotNull SlimePlayer player);

}
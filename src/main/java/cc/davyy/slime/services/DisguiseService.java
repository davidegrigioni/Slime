package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public interface DisguiseService {

    void setNicknamedDisguise(@NotNull SlimePlayer player, @NotNull String nickName);

    void setEntityDisguise(@NotNull SlimePlayer player, @NotNull EntityType entityType);

    void removeNicknamedDisguise(@NotNull SlimePlayer player);

    void removeEntityDisguise(@NotNull SlimePlayer player);

}
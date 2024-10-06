package cc.davyy.slime.services.entities;

import cc.davyy.slime.database.entities.Disguise;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public interface DisguiseService {

    void disguise(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull String nickName);

    void undisguise(@NotNull SlimePlayer player);

    Disguise getPlayerDisguise(@NotNull SlimePlayer player) throws SQLException;

    void saveDisguiseOnLeave(@NotNull SlimePlayer player);

    void reapplyDisguise(@NotNull SlimePlayer player, @NotNull Disguise disguise);

    boolean isPlayerDisguised(@NotNull SlimePlayer player);

}
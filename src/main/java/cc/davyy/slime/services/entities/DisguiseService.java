package cc.davyy.slime.services.entities;

import cc.davyy.slime.database.entities.Disguise;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DisguiseService {

    void disguise(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull String nickName);

    void undisguise(@NotNull SlimePlayer player);

    CompletableFuture<Optional<Disguise>> getPlayerDisguise(@NotNull SlimePlayer player) throws SQLException;

    CompletableFuture<Void> saveDisguiseOnLeave(@NotNull SlimePlayer player);

    void reapplyDisguise(@NotNull SlimePlayer player, @NotNull Disguise disguise);

    CompletableFuture<Boolean> isPlayerDisguised(@NotNull SlimePlayer player);

}
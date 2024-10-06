package cc.davyy.slime.managers.entities;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.database.DisguiseDatabase;
import cc.davyy.slime.database.entities.Disguise;
import cc.davyy.slime.managers.general.SkinManager;
import cc.davyy.slime.model.Messages;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.entities.DisguiseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@Singleton
public class DisguiseManager implements DisguiseService {

    private static final String ENTITY = "entity";
    private static final String NICKNAME = "nickname";

    private final DisguiseDatabase disguiseDatabase;
    private final SkinManager skinManager;

    @Inject
    public DisguiseManager(DisguiseDatabase disguiseDatabase, SkinManager skinManager) {
        this.disguiseDatabase = disguiseDatabase;
        this.skinManager = skinManager;
    }

    @Override
    public void disguise(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull String nickName) {
        if (player.hasTag(TagConstants.DISGUISE_TAG)) {
            player.sendMessage(Messages.ALREADY_DISGUISED
                    .asComponent());
            return;
        }

        Disguise disguise = new Disguise();
        disguise.setPlayerId(player.getUuid().toString());

        switch (disguiseType(nickName, entityType)) {
            case NICKNAME -> disguiseAsNickname(player, nickName, disguise);
            case ENTITY -> disguiseAsEntity(player, entityType, disguise);
        }
    }

    @Override
    public void undisguise(@NotNull SlimePlayer player) {
        if (!player.hasTag(TagConstants.DISGUISE_TAG)) {
            player.sendMessage(Messages.NOT_DISGUISED
                    .asComponent());
            return;
        }

        removeDisguise(player);

        final String disguiseType = player.getTag(TagConstants.DISGUISE_TAG);

        switch (disguiseType) {
            case ENTITY -> resetToOriginalEntityType(player);
            case NICKNAME -> resetToOriginalNickname(player);
        }

        player.removeTag(TagConstants.DISGUISE_TAG);

        player.sendMessage(Messages.DISGUISE_REMOVED
                .asComponent());
    }

    @Override
    public Disguise getPlayerDisguise(@NotNull SlimePlayer player) throws SQLException {
        return disguiseDatabase.getDisguise(player.getUuid().toString());
    }

    @Override
    public boolean isPlayerDisguised(@NotNull SlimePlayer player) {
        return player.hasVanish();
    }

    @Override
    public void reapplyDisguise(@NotNull SlimePlayer player, @NotNull Disguise disguise) {
        if (disguise.getDisguiseType().equals(NICKNAME)) {
            disguiseAsNickname(player, disguise.getNickname(), disguise);
        } else if (disguise.getDisguiseType().equals(ENTITY)) {
            EntityType entityType = EntityType.fromNamespaceId(disguise.getEntityType());
            disguiseAsEntity(player, entityType, disguise);
        }
    }

    @Override
    public void saveDisguiseOnLeave(@NotNull SlimePlayer player) {
        try {
            final Disguise disguise = disguiseDatabase.getDisguise(player.getUuid().toString());
            if (disguise != null) {
                saveDisguise(player, disguise);
            }
        } catch (SQLException e) {
            player.sendMessage("Failed to save disguise.");
        }
    }

    private String disguiseType(@NotNull String nickName, @NotNull EntityType entityType) {
        if (!nickName.isEmpty()) {
            return NICKNAME;
        } else {
            return ENTITY;
        }
    }

    private void disguiseAsNickname(@NotNull SlimePlayer player, @NotNull String nickName, @NotNull Disguise disguise) {
        final CompletableFuture<PlayerSkin> skinFuture = skinManager.getSkinFromUsernameAsync(nickName);

        skinFuture.thenAccept(skin -> {
            if (skin != null) {
                player.setSkin(skin);
                player.setDisplayName(Component.text(nickName));
                player.setTag(TagConstants.DISGUISE_TAG, NICKNAME);

                disguise.setDisguiseType(NICKNAME);
                disguise.setNickname(nickName);
                saveDisguise(player, disguise);

                player.sendMessage(Messages.DISGUISE_NICKNAME
                        .addPlaceholder("nickname", nickName)
                        .asComponent());
            }
        });
    }

    private void disguiseAsEntity(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull Disguise disguise) {
        player.switchEntityType(entityType);
        player.setTag(TagConstants.DISGUISE_TAG, ENTITY);

        disguise.setDisguiseType(ENTITY);
        disguise.setEntityType(entityType.name());
        saveDisguise(player, disguise);

        player.sendMessage(Messages.DISGUISE_ENTITY
                .addPlaceholder("entity", entityType.namespace().value())
                .asComponent());
    }

    private void saveDisguise(@NotNull SlimePlayer player, @NotNull Disguise disguise) {
        try {
            disguiseDatabase.saveDisguise(disguise);
        } catch (SQLException e) {
            player.sendMessage("Failed to save disguise to the database.");
        }
    }

    private void removeDisguise(@NotNull SlimePlayer player) {
        try {
            disguiseDatabase.removeDisguise(player.getUuid().toString());
        } catch (SQLException e) {
            player.sendMessage("Failed to remove disguise from the database.");
        }
    }

    private void resetToOriginalNickname(@NotNull SlimePlayer player) {
        final CompletableFuture<PlayerSkin> originalSkin = skinManager.getSkinFromUsernameAsync(player.getUsername());

        originalSkin.thenAccept(skin -> {
            player.setSkin(skin);
            player.setDisplayName(Component.text(player.getUsername()));
        });

    }

    private void resetToOriginalEntityType(@NotNull SlimePlayer player) {
        player.switchEntityType(EntityType.PLAYER);
    }

}
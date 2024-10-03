package cc.davyy.slime.managers.entities;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.database.DisguiseDatabase;
import cc.davyy.slime.database.entities.Disguise;
import cc.davyy.slime.managers.SkinManager;
import cc.davyy.slime.model.Messages;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.DisguiseService;
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

        try {
            disguiseDatabase.removeDisguise(player.getUuid().toString());
        } catch (SQLException e) {
            player.sendMessage("Failed to remove disguise from the database.");
        }

        player.removeTag(TagConstants.DISGUISE_TAG);
        player.sendMessage(Messages.DISGUISE_REMOVED
                .asComponent());
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

    private void disguiseAsEntity(@NotNull SlimePlayer player, @NotNull EntityType entityType, Disguise disguise) {
        player.switchEntityType(entityType);
        player.setTag(TagConstants.DISGUISE_TAG, ENTITY);

        disguise.setDisguiseType(ENTITY);
        disguise.setEntityType(entityType.name());
        saveDisguise(player, disguise);

        player.sendMessage(Messages.DISGUISE_ENTITY
                .addPlaceholder("entity", entityType.namespace().value())
                .asComponent());
    }

    private void saveDisguise(@NotNull SlimePlayer player, Disguise disguise) {
        try {
            disguiseDatabase.saveDisguise(disguise);
        } catch (SQLException e) {
            player.sendMessage("Failed to save disguise to the database.");
        }
    }

}
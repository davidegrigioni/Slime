package cc.davyy.slime.managers.entities;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.managers.SkinManager;
import cc.davyy.slime.model.SlimePlayer;
import cc.davyy.slime.services.DisguiseService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Singleton
public class DisguiseManager implements DisguiseService {

    private final SkinManager skinManager;

    @Inject
    public DisguiseManager(SkinManager skinManager) {
        this.skinManager = skinManager;
    }

    @Override
    public void setNicknamedDisguise(@NotNull SlimePlayer player, @NotNull String nickName) {
        if (player.hasTag(TagConstants.NICKNAME_TAG)) {
            player.sendMessage(Component.text("You are already disguised with a nickname!").color(NamedTextColor.RED));
            return;
        }

        CompletableFuture<PlayerSkin> skinFuture = skinManager.getSkinFromUsernameAsync(nickName);

        skinFuture.thenAccept(skin -> {
            if (skin != null) {
                player.setSkin(PlayerSkin.fromUsername(nickName));
                player.setDisplayName(Component.text(nickName));
                player.setTag(TagConstants.NICKNAME_TAG, nickName);
                player.sendMessage(Component.text("You are now disguised as " + nickName + "!").color(NamedTextColor.GREEN));
            }
        });
    }

    @Override
    public void setEntityDisguise(@NotNull SlimePlayer player, @NotNull EntityType entityType) {
        if (player.hasTag(TagConstants.ENTITY_TYPE_TAG)) {
            player.sendMessage(Component.text("You are already disguised as an entity!").color(NamedTextColor.RED));
            return;
        }

        player.switchEntityType(entityType);
        player.setTag(TagConstants.ENTITY_TYPE_TAG, entityType);

        player.sendMessage(Component.text("You are now disguised as a " + entityType.name() + "!").color(NamedTextColor.GREEN));
    }

    @Override
    public void removeNicknamedDisguise(@NotNull SlimePlayer player) {
        if (!player.hasTag(TagConstants.NICKNAME_TAG)) {
            player.sendMessage(Component.text("You don't have a nickname disguise applied.").color(NamedTextColor.RED));
            return;
        }

        player.setSkin(PlayerSkin.fromUuid(player.getUuid().toString()));
        player.setDisplayName(Component.text(player.getUsername()));
        player.removeTag(TagConstants.NICKNAME_TAG);

        player.sendMessage(Component.text("Your nickname disguise has been removed.").color(NamedTextColor.YELLOW));
    }

    @Override
    public void removeEntityDisguise(@NotNull SlimePlayer player) {
        if (!player.hasTag(TagConstants.ENTITY_TYPE_TAG)) {
            player.sendMessage(Component.text("You don't have an entity disguise applied.").color(NamedTextColor.RED));
            return;
        }

        player.switchEntityType(EntityType.PLAYER);
        player.removeTag(TagConstants.ENTITY_TYPE_TAG);

        player.sendMessage(Component.text("Your entity disguise has been removed.").color(NamedTextColor.YELLOW));
    }

}
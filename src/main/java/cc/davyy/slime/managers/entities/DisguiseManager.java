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
    public void disguise(@NotNull SlimePlayer player, @NotNull EntityType entityType, @NotNull String nickName) {
        if (player.hasTag(TagConstants.DISGUISE_TAG)) {
            player.sendMessage("You are already disguised.");
            return;
        }

        // Use a better way to check if the player wants to disguise as nickname or entity
        if (!nickName.isEmpty()) {
            // Disguising as a nickname
            final CompletableFuture<PlayerSkin> skinFuture = skinManager.getSkinFromUsernameAsync(nickName);

            skinFuture.thenAccept(skin -> {
                if (skin != null) {
                    player.setSkin(skin);
                    player.setDisplayName(Component.text(nickName));
                    player.setTag(TagConstants.NICKNAME_TAG, nickName);
                    player.setTag(TagConstants.DISGUISE_TAG, "nickname"); // Set disguise type
                    player.sendMessage(Component.text("You are now disguised as " + nickName + "!")
                            .color(NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text("Failed to retrieve skin for " + nickName)
                            .color(NamedTextColor.RED));
                }
            }).exceptionally(ex -> {
                player.sendMessage(Component.text("An error occurred while retrieving skin for " + nickName)
                        .color(NamedTextColor.RED));
                return null;
            });

        } else {
            // Disguising as an entity
            player.switchEntityType(entityType);
            player.setTag(TagConstants.ENTITY_TYPE_TAG, entityType);
            player.setTag(TagConstants.DISGUISE_TAG, "entity"); // Set disguise type

            player.sendMessage(Component.text("You are now disguised as a " + entityType.name() + "!")
                    .color(NamedTextColor.GREEN));
        }
    }

    @Override
    public void undisguise(@NotNull SlimePlayer player) {
        if (!player.hasTag(TagConstants.DISGUISE_TAG)) {
            player.sendMessage("You are not disguised.");
            return;
        }

        final String disguiseType = player.getTag(TagConstants.DISGUISE_TAG);
        switch (disguiseType) {
            case "nickname" -> {
                player.setSkin(PlayerSkin.fromUuid(player.getUuid().toString())); // Restore original skin
                player.setDisplayName(Component.text(player.getUsername()));
                player.removeTag(TagConstants.NICKNAME_TAG);
                player.removeTag(TagConstants.DISGUISE_TAG); // Remove disguise tag

                player.sendMessage(Component.text("Your nickname disguise has been removed.")
                        .color(NamedTextColor.YELLOW));
            }
            case "entity" -> {
                player.switchEntityType(EntityType.PLAYER); // Switch back to the player type
                player.removeTag(TagConstants.ENTITY_TYPE_TAG);
                player.removeTag(TagConstants.DISGUISE_TAG); // Remove disguise tag

                player.sendMessage(Component.text("Your entity disguise has been removed.")
                        .color(NamedTextColor.YELLOW));
            }
            default -> player.sendMessage(Component.text("Unknown disguise type.")
                    .color(NamedTextColor.RED));
        }
    }

}
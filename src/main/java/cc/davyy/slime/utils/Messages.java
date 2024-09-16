package cc.davyy.slime.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cc.davyy.slime.utils.FileUtils.getMessages;

public enum Messages implements ComponentLike {

    RELOAD_CONFIG("messages.reload"),
    MESSAGE_EMPTY("messages.message-empty"),
    NO_PERMS("messages.no-permission"),
    TARGET_PLAYER_NOT_FOUND("messages.target-player-not-found"),

    PET_COSMETIC_CREATED("messages.pet-cosmetic-created"),
    ITEM_COSMETIC_CREATED("messages.item-cosmetic-created"),
    COSMETIC_ID_NOT_FOUND("messages.cosmetic-id-not-found"),
    COSMETIC_APPLIED("messages.cosmetic-applied"),
    COSMETIC_REMOVED("messages.cosmetic-removed"),

    HOLOGRAM_CREATED("messages.hologram-created"),
    HOLOGRAM_DOES_NOT_EXISTS("messages.hologram-does-not-exists"),
    HOLOGRAM_DELETED("messages.hologram-deleted"),
    HOLOGRAM_MOVED("messages.hologram-moved"),

    SPAWN_TELEPORT("messages.spawn-teleport"),
    SPAWN_SET("messages.spawn-set"),

    LOBBY_NOT_FOUND("messages.lobby-not-found"),
    ALREADY_IN_LOBBY("messages.already-in-lobby"),
    FAILED_LOBBY_TELEPORT("messages.failed-lobby-teleport"),
    TELEPORT_TO_LOBBY("messages.teleport-to-lobby"),
    FAILED_TELEPORT_MAIN_LOBBY("messages.failed-teleport-main-lobby"),
    TELEPORT_TO_MAIN_LOBBY("messages.teleport-to-main-lobby"),
    ALREADY_IN_MAIN_LOBBY("messages.already-in-main-lobby"),

    GAMEMODE_SWITCH("messages.gamemode-switch"),
    GAMEMODE_SWITCH_OTHER_SELF("messages.gamemode-switch-other-self"),
    GAMEMODE_SWITCH_OTHER("messages.gamemode-switch-other");

    private final String key;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final List<TagResolver> minimessagePlaceholders = new ArrayList<>();

    Messages(String key) {
        this.key = key;
    }

    @Override
    public @NotNull Component asComponent() {
        String rawMessage = Optional.ofNullable(getMessages().getString(key))
                .orElse("Message not found for key: " + key);
        return miniMessage.deserialize(rawMessage, minimessagePlaceholders.toArray(new TagResolver[0]));
    }

    public @NotNull Messages addPlaceholder(@Subst("test_placeholder") @NotNull String placeholder, @NotNull ComponentLike value) {
        minimessagePlaceholders.add(Placeholder.component(placeholder, value));
        return this;
    }

    public @NotNull Messages addPlaceholder(@Subst("test_placeholder") @NotNull String placeholder, @NotNull String value) {
        minimessagePlaceholders.add(Placeholder.component(placeholder, ColorUtils.of(value).parseLegacy().build()));
        return this;
    }

}
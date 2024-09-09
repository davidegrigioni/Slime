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

    RELOAD_CONFIG("messages.configReloaded"),
    GAMEMODE_SWITCH("messages.gamemodeSwitchSelf"),
    GAMEMODE_OTHER_CHANGED("messages.gamemodeSwitch"),
    GAMEMODE_OTHER_MESSAGE("messages.gamemodeSwitchOther"),
    CANNOT_EXECUTE_FROM_CONSOLE("messages.cannotExecuteFromConsole"),
    MESSAGE_EMPTY("messages.messageEmpty"),
    REGION_SETUP("messages.regionSetupStarted"),
    REGION_SETUP_FIRST("messages.regionSetupRequired"),
    SPAWN_TELEPORT("messages.spawnTeleport"),
    SPAWN_SET("messages.spawnSet");

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
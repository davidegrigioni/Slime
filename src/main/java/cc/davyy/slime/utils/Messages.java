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

import static cc.davyy.slime.utils.FileUtils.getMessages;

public enum Messages implements ComponentLike {

    RELOAD_CONFIG("messages.reload"),
    SPAWN_TELEPORT("messages.spawn-teleport"),
    SPAWN_SET("messages.spawn-set");

    private final String key;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final List<TagResolver> minimessagePlaceholders = new ArrayList<>();

    Messages(String key) {
        this.key = key;
    }

    public @NotNull String getKey() { return key; }

    @Override
    public @NotNull Component asComponent() {
        String rawMessage = getMessages().getString(key);
        return mm.deserialize(rawMessage, minimessagePlaceholders.toArray(new TagResolver[0]));
    }

    public @NotNull Messages addPlaceholder(@Subst("test_placeholder") @NotNull String placeholder, @NotNull ComponentLike value) {
        this.minimessagePlaceholders.add(
                Placeholder.component(placeholder, value)
        );
        return this;
    }

    public @NotNull Messages addPlaceholder(@Subst("test_placeholder") @NotNull String placeholder, @NotNull String value) {
        this.minimessagePlaceholders.add(
                Placeholder.component(placeholder, ColorUtils.of(value)
                        .build())
        );
        return this;
    }

}